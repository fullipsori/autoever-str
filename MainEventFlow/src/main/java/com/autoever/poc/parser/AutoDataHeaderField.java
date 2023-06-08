package com.autoever.poc.parser;

public enum AutoDataHeaderField {
	DataLength("DataLength", 1), 
	DeltaTime("DeltaTime", 4), 
	DataFlag("DataFlag", 1), 
	DataChannel("DataChannel", 1),
	DataID("DataID", 4);

	private final String name;
    private final int size;

    AutoDataHeaderField(String name, int size) { 
    	this.name = name;
    	this.size= size; 
    }

    public String getName() { return this.name; }
    public int getsize() { return this.size; }

}
