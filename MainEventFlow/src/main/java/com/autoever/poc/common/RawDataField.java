package com.autoever.poc.common;

public enum RawDataField {

	DataChannel(0),
	DeltaTime(1),
	MSGInfo(2),
	DataID(3),
	DLC(4),
	DATA(5),
	BaseTime(6);

	final private int index;
	
	private RawDataField(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
}
