package com.autoever.poc.parser;

import com.streambase.sb.Tuple;

public interface PreProcessable {
	public void initialize(Tuple kafkaMessage);
	public boolean preProcess(Tuple kafkaMessage, Tuple dataTuple, int channel, int id, byte[] rawData);
}
