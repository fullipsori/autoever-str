package com.autoever.poc.parser.ccp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Tuple;
import com.streambase.sb.client.CustomFunctionResolver;

public class CustomUtils {

	public static long GetFieldValue(Tuple tuple, int index) {
		try {
			return tuple.getLong(index);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@CustomFunctionResolver("GetDeltaVolCustomUtilsResolver0")
	public static long GetDeltaVol(List<Tuple> cellDatas){
		if(cellDatas == null) return 0;
		return cellDatas.stream().map(tuple -> CustomUtils.GetFieldValue(tuple, 1)).mapToLong(v->v).max().getAsLong()
				- cellDatas.stream().map(tuple -> CustomUtils.GetFieldValue(tuple, 1)).mapToLong(v-> v).min().getAsLong();
	}

	public static CompleteDataType GetDeltaVolCustomUtilsResolver0(CompleteDataType cellDatas) {
		return CompleteDataType.forLong();
	}
	
	@CustomFunctionResolver("GetDVolCustomUtilsResolver0")
	public static List<Double> GetDVol(List<Tuple> cellDatas){
		// TODO Implement function here
		if(cellDatas == null) return null;
		double[] cellDiffs = new double[cellDatas.size()];
		for(int i=0; i < cellDatas.size(); i++) {
			if(i==0) { 
				cellDiffs[i] = 0;
			} else { 
				cellDiffs[i] = GetFieldValue(cellDatas.get(i), 1) - cellDiffs[i-1]; 
			}
		}
		
		double mean = Math.round(Arrays.stream(cellDiffs).average().orElse(0.0) * 10.0)/10.0;
		return Arrays.stream(cellDiffs).map(v -> v-mean).boxed().collect(Collectors.toList());
	}
	
	public static CompleteDataType GetDVolCustomUtilsResolver0(CompleteDataType cellDatas) {
		return CompleteDataType.forList(CompleteDataType.forDouble());
	}

	@CustomFunctionResolver("JoinFromCellTuplesCustomUtilsResolver0")
	public static String JoinFromCellTuples(List<Tuple> cellDatas){
		if(cellDatas == null) return "";
		return cellDatas.stream().map(tuple -> GetFieldValue(tuple, 1)).map(String::valueOf).collect(Collectors.joining(","));
	}

	public static CompleteDataType JoinFromCellTuplesCustomUtilsResolver0(CompleteDataType cellDatas) {
		return CompleteDataType.forString();
	}

	@CustomFunctionResolver("JoinFromNumberListCustomUtilsResolver0")
	public static String JoinFromNumberList(List<? extends Number> datas){
		if(datas == null) return "";
		return datas.stream().map(v-> String.valueOf(v)).collect(Collectors.joining(","));
	}

	public static CompleteDataType JoinFromNumberListCustomUtilsResolver0(CompleteDataType datas) {
		return CompleteDataType.forString();
	}
}
