package com.autoever.poc.parser.can;


import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.common.RawDataField;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Base64;

public class CanPreProcessor implements PreProcessable {

	public CanPreProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static ArrayList<Schema.Field> getSchemaFields(Schema baseSchema) {
		ArrayList<Schema.Field> outputSchemaField = new ArrayList<>(baseSchema.fields());
		return outputSchemaField;
	}

	@Override
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param) {
		// TODO Auto-generated method stub
		PolicyParser policy = PolicyRepository.getInstance().mPolicyMap.get(param);
		if(policy == null) return false;
		return policy.IsAvailable(dataTuple);
	}

	@Override
	public boolean preProcess(Tuple kafkaMessage, Tuple inputTuple, List<Tuple> tuples, Object[] parsed, Schema outputSchema) {
		// TODO Auto-generated method stub
		try {
			String param =  kafkaMessage.getString(AutoKafkaField.TerminalID.getName());
			PolicyParser policy = PolicyRepository.getInstance().mPolicyMap.get(param);
			if(policy == null) return false;
			if(policy.IsAvailable((int)parsed[RawDataField.DataChannel.getValue()], (int)parsed[RawDataField.DataID.getValue()])) {
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
				dataTuple.setTuple("PassThroughs", inputTuple);
				
				tuples.add(dataTuple);
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public void initialize(Tuple kafkaMessage) {
		try {
			String terminalID = kafkaMessage.getString(AutoKafkaField.TerminalID.getName());
			int rootCount = kafkaMessage.getInt(AutoKafkaField.RootCount.getName());
				
			PolicyParser policyParser = PolicyRepository.getInstance().mPolicyMap.get(terminalID);
			if(policyParser != null) policyParser.InitParams(rootCount);
		}catch(Exception e) {}
	}


}
