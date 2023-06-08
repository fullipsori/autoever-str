package com.autoever.poc.common;

public class StringUtils {

	public StringUtils() {}
	
	public static String TestString() {
	}
	public static byte[] mergeByteArray(byte[] first, byte[] second) {
		byte[] combined = new byte[first.length + second.length];
		
		System.arraycopy(first, 0, combined, 0, first.length);
		System.arraycopy(second, 0, combined, first.length, second.length);
		return combined;
	}
}
