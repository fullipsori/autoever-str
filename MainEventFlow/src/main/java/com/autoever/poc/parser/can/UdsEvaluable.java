package com.autoever.poc.parser.can;

import com.streambase.sb.Tuple;

public class UdsEvaluable extends Evaluable {

	public UdsEvaluable(TriggerParser trigger) {
		super(trigger);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object EvalData(TriggerParser trigger, Tuple message) {
		// TODO Auto-generated method stub
		return TriggerParser.EvalUDS(trigger, message);
	}

}
