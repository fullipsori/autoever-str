package com.autoever.poc.parser.gps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.common.TimeUtils;
import com.autoever.poc.parser.AutoGPSField;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.Parseable;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public class GPSData implements Parseable {
	
	private Tuple kafkaMessage;
	private byte[] hcpMessage;
	
	// for data Schema
	private Schema dataSchema;
	private Schema.Field realTime;
	private Schema.Field RealTime;
	private Schema.Field DataFlag;
	private Schema.Field DataChannel;
	private Schema.Field DataID;
	private Schema.Field Latitude;
	private Schema.Field Longitude;
	private Schema.Field Heading;
	private Schema.Field Velocity;
	private Schema.Field Altitude;
	private Schema.Field NS;
	private Schema.Field EW;
	
	
	// { DeltaTime, DataFlag, DataChannel, DataID, Latitude, Longitude, Heading, Velocity, Altitude }
	List<Tuple> gpsData = new ArrayList<Tuple>();
	
	public void typecheck() throws Exception {

		realTime = this.dataSchema.getField("realTime");
		realTime.checkType(CompleteDataType.forLong());
		RealTime = this.dataSchema.getField("RealTime");
		RealTime.checkType(CompleteDataType.forString());
		DataFlag = this.dataSchema.getField("DataFlag");
		DataFlag.checkType(CompleteDataType.forInt());
		DataChannel = this.dataSchema.getField("DataChannel");
		DataChannel.checkType(CompleteDataType.forInt());
		DataID = this.dataSchema.getField("DataID");
		DataID.checkType(CompleteDataType.forInt());

		Latitude = this.dataSchema.getField("Latitude");
		Latitude.checkType(CompleteDataType.forDouble());
		Longitude = this.dataSchema.getField("Longitude");
		Longitude.checkType(CompleteDataType.forDouble());
		Heading = this.dataSchema.getField("Heading");
		Heading.checkType(CompleteDataType.forInt());
		Velocity = this.dataSchema.getField("Velocity");
		Velocity.checkType(CompleteDataType.forDouble());
		Altitude = this.dataSchema.getField("Altitude");
		Altitude.checkType(CompleteDataType.forDouble());
		NS = this.dataSchema.getField("NS");
		EW = this.dataSchema.getField("EW");
	}

	public GPSData(Tuple kafkaMessage, byte[] message, Schema dataSchema) {
		this.kafkaMessage = kafkaMessage;
		this.hcpMessage = message;
		this.dataSchema = dataSchema;
		parse();
	}
	
	public GPSData(byte[] message, Schema dataSchema) {
		this.kafkaMessage = null;
		this.hcpMessage = message;
		this.dataSchema = dataSchema;
		if(message != null)
			parse();
	}

	public GPSData(String filePath, Schema dataSchema) {
		this.kafkaMessage = null;
		this.dataSchema = dataSchema;
		
		byte[] allBytes;
		try {
			allBytes = Files.readAllBytes(Paths.get(filePath));
			if(allBytes != null && allBytes.length > 0) {
				this.hcpMessage = allBytes;
				parse();
			} else {
				this.hcpMessage = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	@Override
	public void parse() {
		
		int[] dlcSize = {0,1,2,3,4,5,6,7,8,12,16,20,24,32,48,64};
		int sIndex = 0;
		final int headerSize = 10;
		try {
			while(sIndex < hcpMessage.length) {
				int dlcIndex = NumUtils.getIntFromBig(hcpMessage, sIndex, 1);
				sIndex += 1; // dlc
				
				int curIndex =  sIndex;

				Tuple gpsTuple = dataSchema.createTuple();
				int tupleIndex = 0;

				long baseTime = (this.kafkaMessage == null)? 0L : kafkaMessage.getLong(AutoKafkaField.BaseTime.getName());
				String localTime = getGPSTime(baseTime, (long)(NumUtils.getLongFromBig(hcpMessage, curIndex, 4)*0.00005));
				gpsTuple.setLong(tupleIndex++, Long.parseLong(localTime));
				gpsTuple.setString(tupleIndex++, localTime);
				curIndex += 4;
				gpsTuple.setInt(tupleIndex++, NumUtils.getIntFromBig(hcpMessage, curIndex, 1));
				curIndex += 1;
				gpsTuple.setInt(tupleIndex++, NumUtils.getIntFromBig(hcpMessage, curIndex, 1));
				curIndex += 1;
				gpsTuple.setInt(tupleIndex++, NumUtils.getIntFromBig(hcpMessage, curIndex, 4));
				curIndex += 4;
				
				int dataSize = dlcSize[dlcIndex];
				if(dataSize > 0) {
					int size = AutoGPSField.Latitude.getsize();
					int latitude = NumUtils.getIntFromBig(hcpMessage,curIndex,size);
					gpsTuple.setDouble(tupleIndex++, getGPSY(latitude));
					curIndex += size;
					size = AutoGPSField.Longitude.getsize();
					int longitude = NumUtils.getIntFromBig(hcpMessage,curIndex,size);
					gpsTuple.setDouble(tupleIndex++, getGPSX(longitude));
					curIndex += size;
					size = AutoGPSField.Heading.getsize();
					gpsTuple.setInt(tupleIndex++, NumUtils.getIntFromBig(hcpMessage,curIndex,size));
					curIndex += size;
					size = AutoGPSField.Velocity.getsize();
					gpsTuple.setDouble(tupleIndex++, (double)NumUtils.getIntFromBig(hcpMessage,curIndex,size));
					curIndex += size;
					size = AutoGPSField.Altitude.getsize();
					gpsTuple.setDouble(tupleIndex++, (double)NumUtils.getIntFromBig(hcpMessage, curIndex, size));
					curIndex += size;
					
					gpsTuple.setString(tupleIndex++, getNS(latitude)? "S" : "N");					
					gpsTuple.setString(tupleIndex++, getEW(longitude)? "E" : "W");
				} else {
					// no gps data 
				}

				gpsData.add(gpsTuple);
				sIndex += (dataSize + headerSize);
			}
		}catch(Exception e) {
//			e.printStackTrace();
			System.out.printf("GPSData Exception:%s", e.getMessage());
			return;
		}
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long data = (long)10.5;
		System.out.println("data:" + data);
//		System.out.println("started main");
//		
		try {
			GPSData gpsDATA = new GPSData("d:/Projects/autoever/vdms/eventflow/workspace/MainEventFlow/src/main/resources/test/GPS.data", null);
//			for(int i=0; i < gpsDATA.gpsData.size(); i++) {
//				Tuple tuple = gpsDATA.gpsData.get(i);
//				try {
//					System.out.printf("index(%d) realTime(%d) RealTime(%s)", i, tuple.getLong(0), tuple.getString(1));
//				} catch (NullValueException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (TupleException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	
//			}
		}catch(Exception e) {
			e.printStackTrace();
		}
//	// int[] = { DeltaTime, DataFlag, DataChannel, DataID, Latitude, Longitude, Heading, Speed, Altitude }
//		System.out.println("Result: " + gpsDATA.gpsData.size());
	}

}
