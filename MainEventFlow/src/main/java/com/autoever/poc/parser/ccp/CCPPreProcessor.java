package com.autoever.poc.parser.ccp;

import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Pair;


public class CCPPreProcessor implements PreProcessable {

	private int prevCmd = 0;
	private Tuple ccpTuple = null;

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
			new Schema.Field("ISOL", CompleteDataType.forDouble())
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
			if(prevCmd != 255 && ((rawdata[0]&0xff) >= 0x0a) && ((rawdata[0]&0xff) <= 0x3b)) {
				prevCmd = rawdata[0] & 0xff;
				return true;
			}
		}
		prevCmd = rawdata[0] & 0xff;
		return false;
	}

	@SuppressWarnings("unchecked")
	public void addTupleField(int command, Pair<String, Long> data) {
		try {

			if(data.first.startsWith("cell_")) {
				Tuple field = FieldType.createTuple();
				field.setString(0, data.first);
				field.setLong(1, data.second);
				if(ccpTuple.isNull("cellData")) {
					List<Tuple> dataList = new ArrayList<>();
					dataList.add(field);
					ccpTuple.setList("cellData", dataList);
				}else {
					List<Tuple> prevData = (List<Tuple>)ccpTuple.getList("cellData");
					List<Tuple> dataList = new ArrayList<>(prevData);
					dataList.add(field);
					ccpTuple.setList("cellData", dataList);
				}
			}else if(data.first.startsWith("msr_tb_")) {
				Tuple field = FieldType.createTuple();
				field.setString(0, data.first);
				field.setLong(1, data.second);
				if(ccpTuple.isNull("msrTBData")) {
					List<Tuple> dataList= new ArrayList<>();
					dataList.add(field);
					ccpTuple.setList("msrTBData", dataList);
				}else {
					List<Tuple> prevData = (List<Tuple>)ccpTuple.getList("msrTBData");
					List<Tuple> dataList = new ArrayList<>(prevData);
					dataList.add(field);
					ccpTuple.setList("msrTBData", dataList);
				}
			}else if("SOC".equals(data.first)) {
				ccpTuple.setDouble("SOC", data.second);
			}else if("msr_data.ibm".equals(data.first)) {
				ccpTuple.setDouble("IBM", data.second);
			}else if("chg_charging_now".equals(data.first)) {
				ccpTuple.setLong("chargingNow", data.second);
			}else if("msr_data.r_isol".equals(data.first)) {
				ccpTuple.setDouble("ISOL", data.second);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean preProcess(Tuple kafkaMessage, Tuple dataTuple, byte[] rawData) {
		// TODO Auto-generated method stub
		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawData[0]&0xff) >= 0x0a) && ((rawData[0]&0xff) <= 0x3b)) {
				prevCmd = rawData[0] & 0xff;
				try {
					String param =  kafkaMessage.getString(AutoKafkaField.TerminalID.getName());
					List<Pair<String, Long>> odtParsed = parseData(rawData, ODTRepository.getInstance().mODTMap.get(param));
					if(odtParsed != null) {
						if(ccpTuple == null) ccpTuple = RawParsed.createTuple();
						odtParsed.stream().forEach(pair -> addTupleField(prevCmd, pair));
					}

					if(prevCmd == 0x3b) {
						dataTuple.setTuple("RawParsed", ccpTuple);
						ccpTuple = null;
						return true; // ccpTuple need to be added.
					}
					return false;
				}catch(Exception e) {
					e.printStackTrace();
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
		ccpTuple = null;
	}

	public Tuple checkProcess(String terminalID, Tuple dataTuple, byte[] rawData) {

		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawData[0]&0xff) >= 0x0a) && ((rawData[0]&0xff) <= 0x3b)) {
				prevCmd = rawData[0] & 0xff;

				try {
					List<Pair<String, Long>> odtParsed = parseData(rawData, ODTRepository.getInstance().mODTMap.get(terminalID));
					if(odtParsed != null) {
						if(ccpTuple == null) ccpTuple = RawParsed.createTuple();
						odtParsed.stream().forEach(pair -> addTupleField(prevCmd, pair));
					}

					if(prevCmd == 0x3b) { //adding tuple.
						Tuple result = ccpTuple;
						ccpTuple = null;
						return result;
					}
					return null;
					
				}catch(Exception e) {
					e.printStackTrace();
					ccpTuple = null;
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
