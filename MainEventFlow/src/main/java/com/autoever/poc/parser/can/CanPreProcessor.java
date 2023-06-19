package com.autoever.poc.parser.can;


import java.util.List;

import com.autoever.poc.adapters.VdmsRawParser.RawParserDataField;
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
			PolicyParser policy = PolicyRepository.getInstance().getMapper(kafkaMessage);
			if(policy == null) return false;

			Tuple rawHeader = dataTuple.getTuple(RawParserDataField.RawHeader.index);
			int ch = rawHeader.getInt(RawDataField.DataChannel.getIndex());
			int id = rawHeader.getInt(RawDataField.DataID.getIndex());
			return policy.IsAvailable(ch, id);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void initialize(Tuple kafkaMessage) {
		try {
			int rootCount = kafkaMessage.getInt(AutoKafkaField.RootCount.getIndex());
			PolicyParser policyParser = PolicyRepository.getInstance().getMapper(kafkaMessage);
			if(policyParser != null) policyParser.InitParams(rootCount);
		}catch(Exception e) {}
	}

	public boolean checkProcess(String terminalID, Tuple dataTuple, byte[] rawData) {
		// TODO Auto-generated method stub
		try {
			PolicyParser policy = PolicyRepository.getInstance().mPolicyMap.get(terminalID);
			if(policy == null) return false;
			int ch = dataTuple.getInt(0);
			int id = dataTuple.getInt(3);
			return policy.IsAvailable(ch, id);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
