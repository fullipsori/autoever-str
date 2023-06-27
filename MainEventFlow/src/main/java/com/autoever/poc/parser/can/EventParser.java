package com.autoever.poc.parser.can;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import com.autoever.poc.parser.DataSavable;
import com.autoever.poc.parser.Parseable;
import com.streambase.sb.Schema;

public class EventParser implements EventCallback, DataSavable {

	private Element mNode;
	public String category;
	public String name;
	public String bitwise;
	public int preTime;
	public int postTime;

	public List<TriggerParser> triggers;
	public List<Object> msgTable = new ArrayList<Object>();

	public Boolean preTriggerCondition = null;


	public EventParser(Element node) {
		this.mNode = node;
		category = mNode.getAttribute("Category");
		name = mNode.getAttribute("Name");
		bitwise = mNode.getAttribute("BIT_WISE");
		preTime = Integer.parseInt(mNode.getAttribute("preData"));
		postTime = Integer.parseInt(mNode.getAttribute("postData"));
		
		triggers = Parseable.GetElements.apply(mNode.getChildNodes(), "Trigger")
			.stream().map(e -> new TriggerParser(e, this))
			.peek(e -> msgTable.add(e.returnVal))
			.collect(Collectors.toList());
	}

	@Override
	public List<String> OnCalled(double time, Boolean status, String value) {
		// TODO Auto-generated method stub
		boolean rvalue = false;
		
		// Boolean == null : only 
		if(value == null) {
			value = "";
		}

		if(status == null) {
			return Arrays.asList(name, String.valueOf(preTime), String.valueOf(postTime), category, "NOMATCH", value);
		}

		if(bitwise.equals("OR")) {
			rvalue = false;
			if(triggers.stream().anyMatch(t -> t.status)) {
				rvalue = true;
			}
		} else {
			rvalue = true;
			if(triggers.stream().anyMatch(t -> !t.status)) {
				rvalue = false;
			}
		}
		
		if(preTriggerCondition != null) {
			if(preTriggerCondition != rvalue && status == false) {
				if(rvalue) {
					preTriggerCondition = rvalue;
					return Arrays.asList(
						name, String.valueOf(preTime), String.valueOf(postTime), category, "OnTrue", value
					);
				}else {
					preTriggerCondition = rvalue;
					return Arrays.asList(
						name, String.valueOf(preTime), String.valueOf(postTime), category, "OnFalse", value
					);
				}
			}
		}
		
		if(status&& rvalue) {
			return Arrays.asList(
				name, String.valueOf(preTime), String.valueOf(postTime), category, "OnChange", value
			);
		}else if(status&& !rvalue) {
			return Arrays.asList(
				name, String.valueOf(preTime), String.valueOf(postTime), category, "OnFalse", value
			);
		}
		
		preTriggerCondition = rvalue;
		return Arrays.asList(name, String.valueOf(preTime), String.valueOf(postTime), category, "RET", value);
	}

	@Override
	public void initData(int param) {
		// TODO Auto-generated method stub
		preTriggerCondition = null;
	}

	@Override
	public Object toSave() {
		// TODO Auto-generated method stub
		return String.format("%s", (preTriggerCondition==null)? "" : preTriggerCondition.toString());
	}

	@Override
	public void fromSave(Object saved) {
		// TODO Auto-generated method stub
		String savedData = (String)saved;
		if(savedData == null || savedData.isEmpty()) preTriggerCondition = null;
		preTriggerCondition = Boolean.parseBoolean(savedData);
	}

	@Override
	public Schema getSaveSchema() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}