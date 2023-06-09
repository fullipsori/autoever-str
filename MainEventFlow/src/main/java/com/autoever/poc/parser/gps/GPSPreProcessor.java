package com.autoever.poc.parser.gps;

import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.common.RawDataField;
import com.autoever.poc.common.TimeUtils;
import com.autoever.poc.parser.AutoGPSField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public class GPSPreProcessor implements PreProcessable {

	public static ArrayList<Schema.Field> getSchemaFields(Schema baseSchema) {
		ArrayList<Schema.Field> outputSchemaField = new ArrayList<>(baseSchema.fields());
		outputSchemaField.add(new Schema.Field("Latitude", CompleteDataType.forDouble()));
		outputSchemaField.add(new Schema.Field("Longitude", CompleteDataType.forDouble()));
		outputSchemaField.add(new Schema.Field("Heading", CompleteDataType.forInt()));
		outputSchemaField.add(new Schema.Field("Velocity", CompleteDataType.forDouble()));
		outputSchemaField.add(new Schema.Field("Altitude", CompleteDataType.forDouble()));
		outputSchemaField.add(new Schema.Field("NS", CompleteDataType.forString()));
		outputSchemaField.add(new Schema.Field("EW", CompleteDataType.forString()));
		return outputSchemaField;
	}

	@Override
	public void initialize(Tuple kafkaMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param) {
		// TODO Auto-generated method stub
		return false;
	}

	public static double getGPSX(int longitude) {
		return (longitude & 0xfffffffe) * 0.0000001;
	}
	
	public static double getGPSY(int latitude) {
		return (latitude & 0xfffffffe) * 0.0000001;
	}
	
	public static boolean getNS(int latitude) {
		return (latitude & 0x01000000) == 0x01000000;
	}
	
	public static boolean getEW(int longitude) {
		return (longitude & 0x01000000) == 0x01000000;
	}
	
	public static String getGPSTime(long baseTime, long deltaTime) {
		/* deltaTime 가 gps 에서는 sec 인지 확인 필요함. */
		return TimeUtils.GetLocalTime(baseTime + deltaTime, null);
	}
	
	@Override
	public int preProcess(Tuple kafkaMessage, Tuple inputTuple, List<Tuple> tuples, Object[] parsed,
			Schema outputSchema) {
		// TODO Auto-generated method stub
		byte[] rawdata = (byte[])parsed[RawDataField.DATA.getValue()];
		
		if(rawdata == null) return 0;

		try {
			Tuple dataTuple = outputSchema.createTuple();

			dataTuple.setInt(RawDataField.DataChannel.getValue(), (int)parsed[RawDataField.DataChannel.getValue()]);
			dataTuple.setDouble(RawDataField.DeltaTime.getValue(), (double)parsed[RawDataField.DeltaTime.getValue()]);
			dataTuple.setInt(RawDataField.MSGInfo.getValue(),  (int)parsed[RawDataField.MSGInfo.getValue()]);
			dataTuple.setInt(RawDataField.DataID.getValue(),  (int)parsed[RawDataField.DataID.getValue()]);
			dataTuple.setInt(RawDataField.DLC.getValue(),  (int)parsed[RawDataField.DLC.getValue()]);
//unused			dataTuple.setString(RawDataField.DATA.getValue(),  Base64.encodeBytes((byte[])parsed[RawDataField.DATA.getValue()]));
			dataTuple.setLong(RawDataField.BaseTime.getValue(),  (long)parsed[RawDataField.BaseTime.getValue()]);
			dataTuple.setBoolean(RawDataField.IsStarted.getValue(),  (boolean)parsed[RawDataField.IsStarted.getValue()]);
			dataTuple.setBoolean(RawDataField.IsEnded.getValue(),  (boolean)parsed[RawDataField.IsEnded.getValue()]);
			dataTuple.setInt(RawDataField.MSGIdx.getValue(), (((int)parsed[RawDataField.MSGIdx.getValue()]) + 1) );

			int curIndex = 0;
			int size = AutoGPSField.Latitude.getsize();
			int latitude = NumUtils.getIntFromBig(rawdata,curIndex,size);
			dataTuple.setDouble("Latitude", getGPSY(latitude));
			curIndex += size;
			size = AutoGPSField.Longitude.getsize();
			int longitude = NumUtils.getIntFromBig(rawdata,curIndex,size);
			dataTuple.setDouble("Longitude", getGPSX(longitude));
			curIndex += size;
			size = AutoGPSField.Heading.getsize();
			dataTuple.setInt("Heading", NumUtils.getIntFromBig(rawdata,curIndex,size));
			curIndex += size;
			size = AutoGPSField.Velocity.getsize();
			dataTuple.setDouble("Velocity", (double)NumUtils.getIntFromBig(rawdata,curIndex,size));
			curIndex += size;
			size = AutoGPSField.Altitude.getsize();
			dataTuple.setDouble("Altitude", (double)NumUtils.getIntFromBig(rawdata, curIndex, size));
			curIndex += size;
			dataTuple.setString("NS", getNS(latitude)? "S" : "N");					
			dataTuple.setString("EW", getEW(longitude)? "E" : "W");
			
			dataTuple.setTuple("PassThroughs", inputTuple);
			tuples.add(dataTuple);
			return 1;
		} catch(Exception e) {
			e.printStackTrace();
		} 

		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
