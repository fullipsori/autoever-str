package com.autoever.poc.common;

public enum RawDataField {

	DataChannel(0),
	DeltaTime(1),
	MSGInfo(2),
	DataID(3),
	DLC(4),
	DATA(5),
	BaseTime(6),
	IsStarted(7),
	IsEnded(8);

	final private int value;
	
	private RawDataField(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
}
