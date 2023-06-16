package com.autoever.poc.parser.can;

import com.streambase.sb.Tuple;

public class Dm1Evaluable extends Evaluable {

	public Dm1Evaluable(TriggerParser trigger) {
		super(trigger);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object EvalData(TriggerParser trigger, Tuple message) {
		// TODO Auto-generated method stub
		return TriggerParser.EvalDM1(trigger, message);
	}


}
