package com.autoever.poc.parser.ccp;

import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.common.RawDataField;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.NullValueException;
import com.streambase.sb.Schema;
import com.streambase.sb.Schema.Field;
import com.streambase.sb.Tuple;
import com.streambase.sb.TupleException;
import com.streambase.sb.util.Base64;
import com.streambase.sb.util.Pair;

public class CCPPreProcessor implements PreProcessable {

	int prevCmd = 0;
	
	public static List<Schema.Field> CCPSchemaField = List.of(
			new Schema.Field("DataChannel", CompleteDataType.forInt()),
			new Schema.Field("DeltaTime", CompleteDataType.forDouble()),
			new Schema.Field("MSGInfo", CompleteDataType.forInt()),
			new Schema.Field("DataID", CompleteDataType.forInt()),
			new Schema.Field("DLC", CompleteDataType.forInt()),
			new Schema.Field("DATA", CompleteDataType.forString()),
			new Schema.Field("BaseTime", CompleteDataType.forLong()),
			new Schema.Field("ODTField", CompleteDataType.forString()),
			new Schema.Field("ODTValue", CompleteDataType.forLong())
	);

	public static ArrayList<Schema.Field> getSchemaFields(Schema baseSchema) {
		ArrayList<Schema.Field> outputSchemaField = new ArrayList<>(baseSchema.fields());
		outputSchemaField.add(new Schema.Field("ODTField", CompleteDataType.forString()));
		outputSchemaField.add(new Schema.Field("ODTValue", CompleteDataType.forLong()));
		return outputSchemaField;
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

	@Override
	public boolean preProcess(Tuple kafkaMessage, Tuple inputTuple, List<Tuple> tuples, Object[] parsed, Schema outputSchema) {
		// TODO Auto-generated method stub
		byte[] rawdata = (byte[])parsed[RawDataField.DATA.getValue()];
		try {
			if(prevCmd != 0 ) {
				if(prevCmd != 255 && ((rawdata[0]&0xff) >= 0x0a) && ((rawdata[0]&0xff) <= 0x3b)) {
					String param =  kafkaMessage.getString(AutoKafkaField.TerminalID.getName());
					List<Pair<String, Long>> odtParsed = parseData(rawdata, ODTRepository.getInstance().mODTMap.get(param));
					if(odtParsed != null) {
						odtParsed.stream().forEach(pair -> {
							try {
								Tuple dataTuple = outputSchema.createTuple();
								dataTuple.setInt(RawDataField.DataChannel.getValue(), (int)parsed[RawDataField.DataChannel.getValue()]);
								dataTuple.setDouble(RawDataField.DeltaTime.getValue(), (double)parsed[RawDataField.DeltaTime.getValue()]);
								dataTuple.setInt(RawDataField.MSGInfo.getValue(),  (int)parsed[RawDataField.MSGInfo.getValue()]);
								dataTuple.setInt(RawDataField.DataID.getValue(),  (int)parsed[RawDataField.DataID.getValue()]);
								dataTuple.setInt(RawDataField.DLC.getValue(),  (int)parsed[RawDataField.DLC.getValue()]);
								dataTuple.setString(RawDataField.DATA.getValue(),  Base64.encodeBytes((byte[])parsed[RawDataField.DATA.getValue()]));
								dataTuple.setLong(RawDataField.BaseTime.getValue(),  (long)parsed[RawDataField.BaseTime.getValue()]);
								dataTuple.setBoolean(RawDataField.IsStarted.getValue(),  (boolean)parsed[RawDataField.IsStarted.getValue()]);
								dataTuple.setBoolean(RawDataField.IsEnded.getValue(),  (boolean)parsed[RawDataField.IsEnded.getValue()]);
								dataTuple.setString("ODTField", pair.first);
								dataTuple.setLong("ODTValue", pair.second);
								dataTuple.setTuple("PassThroughs", inputTuple);
								
								tuples.add(dataTuple);
							}catch(Exception e) {
								e.printStackTrace();
							}
						});
					}
					return true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prevCmd = rawdata[0] & 0xff;
		}
		return false;
	}

	@Override
	public void initialize(Tuple kafkaMessage) {
		// TODO Auto-generated method stub
		prevCmd = 0;
	}


	@SuppressWarnings("unchecked")
	private List<Pair<String, Long>> parseData(byte[] rawdata, ODTParser evtParser) {

		if(evtParser == null) return null;

		int cmd = rawdata[0] & 0xff;
		Object[] odt = evtParser.odt_map.get(cmd-10);
		List<Pair<String,Long>> resultList = new ArrayList<>();
		if(((List<?>)odt[0]).isEmpty()) return null;

		int index = 1;
		for(int i=0; i<((String)odt[2]).length(); i++) {
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
