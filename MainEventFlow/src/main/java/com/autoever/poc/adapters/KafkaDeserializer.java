//
// Copyright 2004-2022 TIBCO Software Inc. All rights reserved.
//

package com.autoever.poc.adapters;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.parser.AutoKafkaField;
import com.streambase.sb.Schema;
import com.streambase.sb.StreamBaseException;
import com.streambase.sb.Tuple;
import com.streambase.sb.TupleException;
import com.streambase.sb.util.Base64;

public class KafkaDeserializer implements org.apache.kafka.common.serialization.Deserializer<Tuple> {

    private static final String CONFIG_SCHEMA = "schema";
    private static final String CONFIG_LOGGER = "logger";
    
    private Schema schema;
    private Logger logger;
    
    private Schema.Field TerminalID;
    private Schema.Field SequenceNo;
    private Schema.Field BodyLength;
    private Schema.Field CIN;
    private Schema.Field VIN;
    private Schema.Field VehicleKeyID;
    private Schema.Field PolicyVersion;
    private Schema.Field RecordCount;
    private Schema.Field RootCount;
    private Schema.Field SubmitSequenceNo;
    private Schema.Field SerialNo;
    private Schema.Field BaseTime;
    private Schema.Field MessageType;

    private Schema.Field FirstPID;
    private Schema.Field MsgSrcKeyId;
    private Schema.Field SyncSerID;
    private Schema.Field LoadDTM;
    private Schema.Field XctRedisInpDTM;
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        schema = (Schema) configs.get(CONFIG_SCHEMA);
        logger = (Logger) configs.get(CONFIG_LOGGER);
        
//        System.out.println("Current Time:" + System.currentTimeMillis());

        try {
			TerminalID = schema.getField(AutoKafkaField.TerminalID.getName());
			SequenceNo = schema.getField(AutoKafkaField.SequenceNo.getName());
			BodyLength = schema.getField(AutoKafkaField.BodyLength.getName());
			CIN = schema.getField(AutoKafkaField.CIN.getName());
			VIN = schema.getField(AutoKafkaField.VIN.getName());
			VehicleKeyID = schema.getField(AutoKafkaField.VehicleKeyID.getName());
			PolicyVersion = schema.getField(AutoKafkaField.PolicyVersion.getName());
			RecordCount = schema.getField(AutoKafkaField.RecordCount.getName());
			RootCount = schema.getField(AutoKafkaField.RootCount.getName());
			SubmitSequenceNo = schema.getField(AutoKafkaField.SubmitSequenceNo.getName());
			SerialNo = schema.getField(AutoKafkaField.SerialNo.getName());
			BaseTime = schema.getField(AutoKafkaField.BaseTime.getName());
			MessageType = schema.getField(AutoKafkaField.MessageType.getName());
			FirstPID = schema.getField(AutoKafkaField.FirstPID.getName());
			MsgSrcKeyId = schema.getField(AutoKafkaField.MsgSrcKeyID.getName());
			SyncSerID = schema.getField(AutoKafkaField.SyncSerID.getName());
			LoadDTM = schema.getField(AutoKafkaField.LoadDTM.getName());
			XctRedisInpDTM = schema.getField(AutoKafkaField.XctRedisInpDTM.getName());
			
		} catch (TupleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public Tuple deserialize(String topic, byte[] data) {
        Tuple tuple = schema.createTuple();
        int curIndex = 0;
        int size;

        if(data == null) {
        	return tuple;
        }

        try {
        	size = AutoKafkaField.TerminalID.getsize();
			tuple.setString(TerminalID, new String(Arrays.copyOfRange(data, curIndex, size)));
			curIndex += size;
        	size = AutoKafkaField.SequenceNo.getsize();
			tuple.setLong(SequenceNo, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.BodyLength.getsize();
			tuple.setLong(BodyLength, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.CIN.getsize();
			tuple.setString(CIN, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.VIN.getsize();
			tuple.setString(VIN, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.VehicleKeyID.getsize();
			tuple.setLong(VehicleKeyID, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.PolicyVersion.getsize();
			tuple.setInt(PolicyVersion, NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.RecordCount.getsize();
			tuple.setLong(RecordCount, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.RootCount.getsize();
			tuple.setInt(RootCount, NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.SubmitSequenceNo.getsize();
			tuple.setLong(SubmitSequenceNo, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.SerialNo.getsize();
			tuple.setString(SerialNo, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.BaseTime.getsize();
			tuple.setLong(BaseTime, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.MessageType.getsize();
			tuple.setInt(MessageType,NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;

        	size = AutoKafkaField.FirstPID.getsize();
			tuple.setString(FirstPID, Base64.encodeBytes(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;

        	size = AutoKafkaField.MsgSrcKeyID.getsize();
			tuple.setString(MsgSrcKeyId, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.SyncSerID.getsize();
			tuple.setString(SyncSerID, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.LoadDTM.getsize();
			tuple.setString(LoadDTM, new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.XctRedisInpDTM.getsize();
			tuple.setLong(XctRedisInpDTM, NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;

        } catch (StreamBaseException e) {
            logger.error("Error deserializing topic '" + topic + "': " + e.getMessage(), e);
        }
        return tuple;
    }

    @Override
    public void close() {
    }

    /**
    public static void main(String[] args) {
		byte[] data = {0x01,0x05,(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};
		System.out.println("hex data:" + StringUtils.convertbytesToHex(data, 0, data.length));
		String dataString;
		try {
			dataString = new String(data, "ISO8859-1");
			byte[] data_2 = dataString.getBytes("ISO8859-1");
			System.out.println("compare:" + Arrays.compare(data, data_2));
			System.out.println("string data:" + StringUtils.convertStrToHexWithCharset(dataString, "ISO8859-1"));
			System.out.println("string_sub data:" + StringUtils.convertbytesToHex(data_2, 0, data_2.length));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	**/
}
