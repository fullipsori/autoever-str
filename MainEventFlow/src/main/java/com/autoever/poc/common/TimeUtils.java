package com.autoever.poc.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

	/**
	* A StreamBase Simple Function. Use this function
	* in StreamBase expressions using the <em>calljava</em> function, or 
	* by an assigned alias. It can then be called directly 
	* using the alias name instead of using calljava().
	*/
	public static String GetLocalTime(long timeSeconds, String format){
	    // TODO Implement function here
		LocalDateTime realTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeSeconds), ZoneId.of("Asia/Seoul"));
		if(format == null || format.isEmpty())
			return realTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		return realTime.format(DateTimeFormatter.ofPattern(format));
	}

	/**
	* A StreamBase Simple Function. Use this function
	* in StreamBase expressions using the <em>calljava</em> function, or 
	* by an assigned alias. It can then be called directly 
	* using the alias name instead of using calljava().
	*/
	public static String GetCurrUTCTime(String format){
	    // TODO Implement function here
		LocalDateTime realTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
		if(format == null || format.isEmpty()) {
			return realTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		} else {
			return realTime.format(DateTimeFormatter.ofPattern(format));
		}
	}

	public static String getUTCTime(long timeSeconds, String format) {
		LocalDateTime realTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeSeconds), ZoneId.of("UTC"));
		if(format == null || format.isEmpty()) {
			return realTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		} else {
			return realTime.format(DateTimeFormatter.ofPattern(format));
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("time:" + GetCurrUTCTime("yyyyMMDD'T'HHmmss'Z'"));
	}
}
