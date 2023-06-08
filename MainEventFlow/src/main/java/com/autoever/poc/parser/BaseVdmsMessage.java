package com.autoever.poc.parser;

import java.util.List;

import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public abstract class BaseVdmsMessage {

	abstract public Schema GetResultSchema();
	abstract public Tuple GenMessage(List<Object> rawMsg, String params);
}
