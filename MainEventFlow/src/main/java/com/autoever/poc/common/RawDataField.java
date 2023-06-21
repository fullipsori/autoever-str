package com.autoever.poc.common;

public enum RawDataField {

	DLC(0),
	DeltaTime(1),
	MSGInfo(2),
	DataChannel(3),
	DataID(4),
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
