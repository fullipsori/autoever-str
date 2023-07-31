package com.autoever.poc.common;

import java.util.ArrayList;
import java.util.List;

import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Tuple;
import com.streambase.sb.client.CustomFunctionResolver;
import com.streambase.sb.operator.AggregateWindow;

public class UniqueDatas extends AggregateWindow {

	public static final long serialVersionUID = 1690338405269L;
	private List<Tuple> accDatas = new ArrayList<>();

	/**
	* Called before each new use of the aggregate window object.
	*/
	public void init() {
		accDatas.clear();
	}

	public List<?> calculate() {
		// TODO Implement function here
		return accDatas;
	}

	@CustomFunctionResolver("accumulateCustomFunctionResolver0")
	public void accumulate(Tuple inData, boolean isFirst) {
		// TODO Implement function here
		if(inData == null) return;
		try {
			String fieldName = inData.getString(0);
			if(fieldName == null) return;
			Tuple tData = accDatas.stream().filter(t -> {
				try {
					return t.getString(0).equals(fieldName);
				} catch (Exception e) {
					return false;
				}
			}).findFirst().orElse(null);
			if(tData == null || !isFirst) {
				accDatas.add(inData);
			}

		}catch(Exception e) { }

	}

	public static CompleteDataType accumulateCustomFunctionResolver0(CompleteDataType inData,
			CompleteDataType isFirst) {
		return CompleteDataType.forList(inData);
	}

	public void release() {
		accDatas.clear();
	}

}
