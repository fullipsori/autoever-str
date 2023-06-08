package com.autoever.poc.parser;

public enum AutoKafkaField {
	TerminalID("TerminalID", 11), 
	SequenceNo("SequenceNo", 4), 
	BodyLength("BodyLength", 4), 
	CIN("CIN", 8), 
	VIN("VIN", 30), 
	VehicleKeyID("VehicleKeyID", 4), 
	PolicyVersion("PolicyVersion", 2),
	RecordCount("RecordCount", 4),
	RootCount("RootCount", 2),
	SubmitSequenceNo("SubmitSequenceNo", 4),
	SerialNo("SerialNo", 11),
	BaseTime("BaseTime", 4),
	MessageType("MessageType", 1),
	FirstPID("FirstPID", 128),
	MsgSrcKeyID("MsgSrcKeyID", 128),
	SyncSerID("SyncSerID", 20),
	LoadDTM("LoadDTM", 14),
	XctRedisInpDTM("XctRedisInpDTM", 4);

	private final String name;
    private final int size;

    AutoKafkaField(String name, int size) { 
    	this.name = name;
    	this.size= size; 
    }

    public String getName() { return this.name; }
    public int getsize() { return this.size; }
}
