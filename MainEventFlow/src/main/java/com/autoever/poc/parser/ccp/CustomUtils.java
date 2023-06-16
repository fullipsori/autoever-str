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
				cellDiffs[i] = GetFieldValue(cellDatas.get(i), 1) - GetFieldValue(cellDatas.get(i-1), 1); 
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

	@CustomFunctionResolver("GetCellTupleDataCustomUtilsResolver0")
	public static List<Long> GetCellTupleData(List<Tuple> cellDatas){
		if(cellDatas == null) return null;
		return cellDatas.stream().map(tuple -> GetFieldValue(tuple, 1)).collect(Collectors.toList());
	}

	public static CompleteDataType GetCellTupleDataCustomUtilsResolver0(CompleteDataType cellDatas) {
		return CompleteDataType.forList(CompleteDataType.forLong());
	}

	@CustomFunctionResolver("JoinFromMsrTBTuplesCustomUtilsResolver0")
	public static String JoinFromMsrTBTuples(List<Tuple> msrTBDatas){
		if(msrTBDatas== null) return "";
		return msrTBDatas.stream().map(tuple -> GetFieldValue(tuple, 1)).map(l -> Math.round(l)/10.0).map(String::valueOf).collect(Collectors.joining(","));
	}

	public static CompleteDataType JoinFromMsrTBTuplesCustomUtilsResolver0(CompleteDataType msrTBDatas) {
		return CompleteDataType.forString();
	}

	@CustomFunctionResolver("GetMsrTBTupleDataCustomUtilsResolver0")
	public static List<Double> GetMsrTBTupleData(List<Tuple> msrTBDatas){
		if(msrTBDatas== null) return null;
		return msrTBDatas.stream().map(tuple -> GetFieldValue(tuple, 1)).map(l -> Math.round(l)/10.0).collect(Collectors.toList());
	}

	public static CompleteDataType GetMsrTBTupleDataCustomUtilsResolver0(CompleteDataType msrTBDatas) {
		return CompleteDataType.forList(CompleteDataType.forDouble());
	}

	@CustomFunctionResolver("JoinFromNumberListCustomUtilsResolver0")
	public static String JoinFromNumberList(List<? extends Number> datas){
		if(datas == null || datas.size() == 0) return "none";
		return datas.stream().map(v-> String.valueOf(v)).collect(Collectors.joining(","));
	}

	public static CompleteDataType JoinFromNumberListCustomUtilsResolver0(CompleteDataType datas) {
		return CompleteDataType.forString();
	}
	
	
	@CustomFunctionResolver("getMatchedTupleByIntervalCustomUtilsResolver0")
	public static Tuple getMatchedTupleByInterval(Tuple kafkaMessage, Tuple dataTuple, double realTime, double minInterval) {
		ODTParser odtParser = ODTRepository.getInstance().getMapper(kafkaMessage);
		if(odtParser != null) return odtParser.getMatchedTupleByInterval(dataTuple, realTime, minInterval);
		return null;
	}
	
	public static CompleteDataType getMatchedTupleByIntervalCustomUtilsResolver0(CompleteDataType kafkaMessage, CompleteDataType dataTuple, CompleteDataType realTime, CompleteDataType minInterval) {
		return CompleteDataType.forTuple(CCPPreProcessor.RawParsed);
	}
	
	
	public static List<Double> checkDVol(List<Integer> cellDatas){
		// TODO Implement function here
		if(cellDatas == null) return null;
		double[] cellDiffs = new double[cellDatas.size()];
		for(int i=0; i < cellDatas.size(); i++) {
			if(i==0) { 
				cellDiffs[i] = 0;
			} else { 
				cellDiffs[i] = cellDatas.get(i)- cellDatas.get(i-1); 
			}
		}
		
		Arrays.stream(cellDiffs).forEach(d -> System.out.printf("%f,", Math.round(d*10.0)/10.0));
		System.out.println("");
		double mean = Math.round(Arrays.stream(cellDiffs).average().orElse(0.0) * 10.0)/10.0;
		System.out.println("mean:" + mean);
		return Arrays.stream(cellDiffs).map(v -> v-mean).boxed().collect(Collectors.toList());
	}
	
	
	public static void main(String[] args) {
		
		List<Integer> data = List.of(
				3595,3595,3596,3595,3596,3595,3596,3595,3593,3593,
				3596,3595,3596,3596,3596,3595,3596,3592,3592,3594,
				3596,3595,3596,3596,3595,3595,3594,3595,3595,3595,
				3595,3596,3595,3596,3594,3593,3595,3595,3595,3595,
				3595,3595,3595,3595,3593,3594,3596,3596,3595,3595,
				3595,3595,3595,3593,3593,3595,3595,3596,3594,3596,
				3595,3597,3591,3595,3595,3595,3595,3595,3595,3594,
				3595,3593,3595,3596,3597,3596,3597,3595,3596,3594,
				3593,3593,3596,3595,3595,3596,3594,3596,3596,3593
		);
		List<Double> result = checkDVol(data);
		System.out.println("");
		result.stream().forEach(d -> System.out.printf("%f,", Math.round(d*10.0)/10.0));
	}
}
