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
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        schema = (Schema) configs.get(CONFIG_SCHEMA);
        logger = (Logger) configs.get(CONFIG_LOGGER);
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
			tuple.setString(AutoKafkaField.TerminalID.getIndex(), new String(Arrays.copyOfRange(data, curIndex, size)));
			curIndex += size;
        	size = AutoKafkaField.SequenceNo.getsize();
			tuple.setLong(AutoKafkaField.SequenceNo.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.BodyLength.getsize();
			tuple.setLong(AutoKafkaField.BodyLength.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.CIN.getsize();
			tuple.setString(AutoKafkaField.CIN.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.VIN.getsize();
			tuple.setString(AutoKafkaField.VIN.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.VehicleKeyID.getsize();
			tuple.setLong(AutoKafkaField.VehicleKeyID.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.PolicyVersion.getsize();
			tuple.setInt(AutoKafkaField.PolicyVersion.getIndex(), NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.RecordCount.getsize();
			tuple.setLong(AutoKafkaField.RecordCount.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.RootCount.getsize();
			tuple.setInt(AutoKafkaField.RootCount.getIndex(), NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.SubmitSequenceNo.getsize();
			tuple.setLong(AutoKafkaField.SubmitSequenceNo.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.SerialNo.getsize();
			tuple.setString(AutoKafkaField.SerialNo.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.BaseTime.getsize();
			tuple.setLong(AutoKafkaField.BaseTime.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
			curIndex += size;
        	size = AutoKafkaField.MessageType.getsize();
			tuple.setInt(AutoKafkaField.MessageType.getIndex(),NumUtils.getIntFromBig(data, curIndex, size));
			curIndex += size;

        	size = AutoKafkaField.FirstPID.getsize();
			tuple.setString(AutoKafkaField.FirstPID.getIndex(), Base64.encodeBytes(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;

        	size = AutoKafkaField.MsgSrcKeyID.getsize();
			tuple.setString(AutoKafkaField.MsgSrcKeyID.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.SyncSerID.getsize();
			tuple.setString(AutoKafkaField.SyncSerID.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.LoadDTM.getsize();
			tuple.setString(AutoKafkaField.LoadDTM.getIndex(), new String(Arrays.copyOfRange(data, curIndex, curIndex+size)));
			curIndex += size;
        	size = AutoKafkaField.XctRedisInpDTM.getsize();
			tuple.setLong(AutoKafkaField.XctRedisInpDTM.getIndex(), NumUtils.getLongFromBig(data, curIndex, size));
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
