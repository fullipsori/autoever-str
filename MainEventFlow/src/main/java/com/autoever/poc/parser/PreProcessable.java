package com.autoever.poc.parser;

import com.streambase.sb.Tuple;

public interface PreProcessable {
	public void initialize(Tuple kafkaMessage);
	public boolean preProcess(Tuple dataTuple, byte[] rawdata, String param);
	public boolean preProcess(Tuple kafkaMessage, Tuple dataTuple, byte[] rawData);
}
