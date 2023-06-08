package com.autoever.poc.parser;


import java.util.List;

import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public interface PreProcessable {
	public void initialize(Tuple kafkaMessage);
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param);
	public boolean preProcess(Tuple kafkaMessage, Tuple inputTuple, List<Tuple> tuples, Object[] parsed, Schema outputSchema);
}
