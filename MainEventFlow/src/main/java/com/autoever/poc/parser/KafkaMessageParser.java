package com.autoever.poc.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import com.autoever.poc.common.NumUtils;

public class KafkaMessageParser implements Parseable {
	private byte[] kafkaMessage;
	private String terminalId;
	private long sequenceNo;
	private long bodyLength;
	private String CIN;
	private String VIN;
	private long vehicleKeyID;
	private int policyVersion;
	private long recordCount;
	private int rootCount;
	private long submitSequenceNo;
	private String serialNo;
	private long baseTime;
	private int messageType;
	
	private String firstPID;
	private String msgSrcKeyID;
	private String syncSerID;
	private String loadDTM;
	private long xctRedisInpDTM;
	
	public KafkaMessageParser(String filePath) {
		
		byte[] allBytes;
		try {
			allBytes = Files.readAllBytes(Paths.get(filePath));
			if(allBytes != null && allBytes.length > 0) {
				this.kafkaMessage = allBytes;
			} else {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
    public void parse() {
        int curIndex = 0;
        int size;

        byte[] data = this.kafkaMessage;

        try {
        	size = AutoKafkaField.TerminalID.getsize();
			terminalId =  new String(Arrays.copyOfRange(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.SequenceNo.getsize();
			sequenceNo = NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.BodyLength.getsize();
			bodyLength = NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.CIN.getsize();
			CIN = new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.VIN.getsize();
			VIN = new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.VehicleKeyID.getsize();
			vehicleKeyID= NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.PolicyVersion.getsize();
			policyVersion = NumUtils.getIntFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.RecordCount.getsize();
			recordCount = NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.RootCount.getsize();
			rootCount = NumUtils.getIntFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.SubmitSequenceNo.getsize();
			submitSequenceNo= NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.SerialNo.getsize();
			serialNo= new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.BaseTime.getsize();
			baseTime= NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
        	size = AutoKafkaField.MessageType.getsize();
			messageType=NumUtils.getIntFromBig(data, curIndex, size);

			curIndex += size;
        	size = AutoKafkaField.FirstPID.getsize();
			firstPID= new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.MsgSrcKeyID.getsize();
			msgSrcKeyID= new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.SyncSerID.getsize();
			syncSerID= new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.LoadDTM.getsize();
			loadDTM= new String(Arrays.copyOfRange(data, curIndex, curIndex+size));
			curIndex += size;
        	size = AutoKafkaField.XctRedisInpDTM.getsize();
			xctRedisInpDTM= NumUtils.getLongFromBig(data, curIndex, size);
			curIndex += size;
			
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return ;
    }

	public static void main(String[] args) {

//		System.out.println("kafkamessage main");
//		KafkaMessageParser parser = new KafkaMessageParser("d:/Projects/autoever/vdms/workspace/MainEventFlow/src/main/resources/vin_{184221_VM-21C-0176_2048}_KNDCT3L10P5000023_VM-21C-0176_20221006030752_vdmssyn02_9_20221006025943_GPS.info");
//		parser.parse();
//		
//		byte[] data = {(byte)0x62,(byte)0xA1,(byte)0x80,(byte)0x65};
//		System.out.printf("data(%s)\n", TimeUtils.getTimeString(NumUtils.getLongFromBig(data, 0, 4)));
	}

}
