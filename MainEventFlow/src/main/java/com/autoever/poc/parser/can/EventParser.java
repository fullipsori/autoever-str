package com.autoever.poc.parser.can;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.autoever.poc.parser.Parseable;

public class EventParser implements EventCallback {

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
	public Object OnCalled(long time, boolean status) {
		// TODO Auto-generated method stub
		boolean rvalue = false;
		
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
						name, String.valueOf(preTime), String.valueOf(postTime), category, "OnTrue"
					);
				}else {
					preTriggerCondition = rvalue;
					return Arrays.asList(
						name, String.valueOf(preTime), String.valueOf(postTime), category, "OnFalse"
					);
				}
			}
		}
		
		if(status&& rvalue) {
			return Arrays.asList(
				name, String.valueOf(preTime), String.valueOf(postTime), category, "OnChange"
			);
		}else if(status&& !rvalue) {
			return Arrays.asList(
				name, String.valueOf(preTime), String.valueOf(postTime), category, "OnFalse"
			);
		}
		
		preTriggerCondition = rvalue;
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}