package com.autoever.poc.parser.can;


import java.util.List;

import com.autoever.poc.common.RawDataField;
import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public class CanPreProcessor implements PreProcessable {

	public CanPreProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static void addSchemaField(List<Schema.Field> outputSchemaField) {
		return;
	}

	@Override
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param) {
		// TODO Auto-generated method stub
		PolicyParser policy = PolicyRepository.getInstance().mPolicyMap.get(param);
		if(policy == null) return false;
		return policy.IsAvailable(dataTuple);
	}

	@Override
	public boolean preProcess(Tuple kafkaMessage, Tuple dataTuple, byte[] rawData) {
		// TODO Auto-generated method stub
		try {
			String param =  kafkaMessage.getString(AutoKafkaField.TerminalID.getName());
			PolicyParser policy = PolicyRepository.getInstance().mPolicyMap.get(param);
			if(policy == null) return false;
			int ch = dataTuple.getTuple("RawHeader").getInt(RawDataField.DataChannel.getValue());
			int id = dataTuple.getTuple("RawHeader").getInt(RawDataField.DataID.getValue());
			return policy.IsAvailable(ch, id);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
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
