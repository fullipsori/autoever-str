package com.autoever.poc.parser.ccp;

import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.common.StringUtils;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Pair;


public class CCPPreProcessor implements PreProcessable {

	private int prevCmd = 0;
	private final int ccpStartCmd = 0x0a;
	private final int ccpEndCmd = 0x3b;
	private final int ccpRawEndCmd = 0x36; // 들어올 수 있는 유효한 데이터의 끝이여야 함.
	private RawParsedData rawParsedData = null;

	private class RawParsedData {
		private List<Tuple> cellData = new ArrayList<>();
		private List<Tuple> msrTBData = new ArrayList<>();
		private Double SOC = null;
		private Double IBM = null;
		private Long chargingNow = null;
		private Double ISOL = null;
		
		private boolean validate() {
			if(cellData.size() != 90) return false;
			if(msrTBData.size() != 9) return false;
			if(SOC == null) return false;
			if(IBM == null) return false;
			if(chargingNow == null) return false;
			if(ISOL == null) return false;
			return true;
		}
		
		public void addFieldData(int command, Pair<String, Long> data) {
			try {
				if(data.first.startsWith("cell_")) {
					Tuple field = FieldType.createTuple();
					field.setString(0, data.first);
					field.setLong(1, data.second);
					cellData.add(field);
				}else if(data.first.startsWith("msr_tb_")) {
					Tuple field = FieldType.createTuple();
					field.setString(0, data.first);
					field.setLong(1, data.second);
					msrTBData.add(field);
				}else if("SOC".equals(data.first)) {
					SOC = data.second * 1.0;
				}else if("msr_data.ibm".equals(data.first)) {
					IBM = data.second * 1.0;
				}else if("chg_charging_now".equals(data.first)) {
					chargingNow = data.second;
				}else if("msr_data.r_isol".equals(data.first)) {
					ISOL =  data.second * 1.0;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public Tuple GetTuple() {
			try {
				if(validate()) {
					Tuple ccpTuple = CCPPreProcessor.RawParsed.createTuple();
					ccpTuple.setList("cellData", cellData);
					ccpTuple.setList("msrTBData", msrTBData);
					ccpTuple.setDouble("SOC", SOC);
					ccpTuple.setDouble("IBM", IBM);
					ccpTuple.setLong("chargingNow", chargingNow);
					ccpTuple.setDouble("ISOL", ISOL);
					return ccpTuple;
				}
				return null;
			}catch(Exception e) {
				return null;
			}
		}
	}

	public final static Schema FieldType = new Schema("FieldType", List.of(
			new Schema.Field("ODTField", CompleteDataType.forString()),
			new Schema.Field("ODTValue", CompleteDataType.forLong())));

	public final static Schema RawParsed = new Schema("RawParsed", 
		List.of(
			new Schema.Field("cellData", CompleteDataType.forList(CompleteDataType.forTuple(FieldType))),
			new Schema.Field("msrTBData", CompleteDataType.forList(CompleteDataType.forTuple(FieldType))),
			new Schema.Field("SOC", CompleteDataType.forDouble()),
			new Schema.Field("IBM", CompleteDataType.forDouble()),
			new Schema.Field("chargingNow", CompleteDataType.forLong()),
			new Schema.Field("ISOL", CompleteDataType.forDouble()),
			new Schema.Field("deltaVol", CompleteDataType.forLong()),
			new Schema.Field("dVols", CompleteDataType.forList(CompleteDataType.forDouble())),
			new Schema.Field("RESs", CompleteDataType.forList(CompleteDataType.forDouble()))
		));
	
	public static void addSchemaField(List<Schema.Field> outputSchemaField) {
		outputSchemaField.add(new Schema.Field("RawParsed", CompleteDataType.forTuple(RawParsed)));
		return;
	}

	public CCPPreProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param) {
		// TODO Auto-generated method stub
		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawdata[0]&0xff) >= ccpStartCmd) && ((rawdata[0]&0xff) <= ccpEndCmd)) {
				prevCmd = rawdata[0] & 0xff;
				return true;
			}
		}
		prevCmd = rawdata[0] & 0xff;
		return false;
	}


	@Override
	public boolean preProcess(Tuple kafkaMessage, Tuple dataTuple, byte[] rawData) {
		// TODO Auto-generated method stub
		if(rawData == null || rawData.length == 0) return false;
		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawData[0]&0xff) >= this.ccpStartCmd) && ((rawData[0]&0xff) <= 0x3b)) {
				prevCmd = rawData[0] & 0xff;
				try {
					List<Pair<String, Long>> odtParsed = parseData(rawData, ODTRepository.getInstance().getMapper(kafkaMessage));
					if(odtParsed != null) {
						if(rawParsedData == null) rawParsedData = new RawParsedData();
						odtParsed.stream().forEach(pair -> rawParsedData.addFieldData(prevCmd, pair));
					}

					if(prevCmd >= this.ccpRawEndCmd && rawParsedData != null) {
						Tuple resTuple = rawParsedData.GetTuple();
						rawParsedData = null;
						if(resTuple != null) {
							dataTuple.setTuple("RawParsed", resTuple); 
							return true;
						}
						return false;
					}
					return false;
				}catch(Exception e) {
					e.printStackTrace();
					rawParsedData = null;
					return false;
				}
			}
		}
		prevCmd = rawData[0] & 0xff;
		return false;
	}


	@Override
	public void initialize(Tuple kafkaMessage) {
		// TODO Auto-generated method stub
		prevCmd = 0;
		rawParsedData = null;
		try {
			int rootCount = kafkaMessage.getInt(AutoKafkaField.RootCount.getIndex());
			ODTParser odtParser = ODTRepository.getInstance().getMapper(kafkaMessage);
			if(odtParser != null) odtParser.InitParams(rootCount);
		}catch(Exception e) {}
	}

	public Tuple checkProcess(long vehicleKeyID, Tuple dataTuple, byte[] rawData) {

		if(rawData == null || rawData.length == 0) return null;

		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawData[0]&0xff) >= ccpStartCmd) && ((rawData[0]&0xff) <= ccpEndCmd)) {
				prevCmd = rawData[0] & 0xff;
				if(prevCmd == 0x14) {
					System.out.println(StringUtils.convertbytesToHex(rawData, 0, rawData.length));
				}
				try {
					List<Pair<String, Long>> odtParsed = parseData(rawData, ODTRepository.getInstance().mODTMap.get(String.valueOf(vehicleKeyID)));
					if(odtParsed != null) {
						if(rawParsedData == null) rawParsedData = new RawParsedData();
						odtParsed.stream().forEach(pair -> rawParsedData.addFieldData(prevCmd, pair));
					}
					if(prevCmd >= ccpRawEndCmd && rawParsedData != null) { //adding tuple.
						Tuple resTuple = rawParsedData.GetTuple();
						rawParsedData = null;
						return resTuple;
					}
					return null;
					
				}catch(Exception e) {
					e.printStackTrace();
					rawParsedData = null;
					return null;
				}
			}
		}
		prevCmd = rawData[0] & 0xff;
		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Pair<String, Long>> parseData(byte[] rawdata, ODTParser evtParser) {

		if(evtParser == null) return null;

		int cmd = rawdata[0] & 0xff;
		Object[] odt = evtParser.odt_map.get(cmd-10);
		List<Pair<String,Long>> resultList = new ArrayList<>();
		if(((List<?>)odt[0]).isEmpty()) return null;

		int index = 1;
		for(int i=0; i<((List<String>)odt[0]).size(); i++) {
			switch(((String)odt[2]).charAt(i)) {
				case 'L' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							NumUtils.getLongFromLittle(rawdata, index, 4)));
					index += 4;
					break;
				case 'H' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							NumUtils.getLongFromLittle(rawdata, index, 2)));
					index += 2;
					break;
				case 'B' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							NumUtils.getLongFromLittle(rawdata, index, 1)));
					index += 1;
					break;
			}
		}
		return resultList;
	}

}
