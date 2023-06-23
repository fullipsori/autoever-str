package com.autoever.poc.parser;

public interface DataSavable {
	public void init();
	public String toSave();
	public void fromSave();
}
