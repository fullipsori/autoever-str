package com.autoever.poc.parser.can;


import java.util.ArrayList;
import java.util.List;

import com.autoever.poc.parser.AutoKafkaField;
import com.autoever.poc.parser.PreProcessable;
import com.autoever.poc.parser.RawDataParser;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Base64;

public class CanPreProcessor implements PreProcessable {

	public CanPreProcessor() {
		// TODO Auto-generated constructor stub
	}

	public static void addSchemaField(List<Schema.Field> outputSchemaField) {
		return;
	}

	@Override
	public boolean preProcess(Tuple inputTuple, Tuple dataTuple, int msgInfo, int channel, int id, byte[] rawData) {
		try {
			Tuple kafkaMessage = inputTuple.getTuple("kafkaMessage");
			PolicyParser policy = PolicyRepository.getInstance().getMapper(kafkaMessage);
			if(policy == null) return false;
			return policy.IsAvailable(channel, id);
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

	public static void main(String[] args) {
		
		String filepath= "d:/projects/vdms/resources/download/VM-21C-0004_177554_1_1687108486_1687135408089.dat";
		PolicyRepository.getInstance().LoadPolicy("d:/projects/vdms/resources/policy", "xml");
		
		List<Tuple> result = new RawDataParser(filepath, 0).getParsed();
		
		if(result.isEmpty()) {
			System.out.println("empty");
			return;
		}
		
		List<Tuple> tuples = new ArrayList<>();
		CanPreProcessor canPreProcessor = new CanPreProcessor();

		long started, ended;
		long elapsed = 0;
		System.out.println("Started:[" + result.size() + "]:-->");
		for(Tuple tuple : result) {
			byte[] rawdata;
			try {
				rawdata = Base64.decode(tuple.getString("DATA"));
				started = System.currentTimeMillis();
				if(canPreProcessor.checkProcess("VM-21C-0004", tuple, rawdata)) {
					tuples.add(tuple);
				}
				ended = System.currentTimeMillis();
				elapsed += ended - started;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.println("result:" + elapsed);

		/**
		PolicyParser parser = PolicyRepository.getInstance().mPolicyMap.get(filename);
		result.stream().forEach(t -> {
			if(parser.GetKeyFlag(t)) {
				List<Tuple> res = parser.GetTrigData(t);
				if(res != null) {
					res.stream().filter(e -> e != null)
					.filter(e -> {
						try {
							return !"NOT".equals(e.getString(5)) && !"RET".equals(e.getString(5));
						} catch (Exception e2) {
							return false;
						}
					})
					.filter(e -> {
						try {
							return !e.getString(5).equals("NOMATCH") && !e.getString(5).equals("RET");
						} catch (Exception e2) {
							return false;
						}
					})
					.forEach(e -> {
						try {
							System.out.println("" + e.getDouble(0) + " " + e.getDouble(1) + " " + e.getDouble(2) + " " + e.getString(3) + " " + e.getString(4) + " " + e.getString(5) + " " + e.getString(6));
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				}
			}
				
		});
		**/
	}

}
