package com.autoever.poc.parser.can;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.autoever.poc.common.RawDataField;
import com.autoever.poc.common.StringUtils;
import com.autoever.poc.parser.Parseable;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Base64;

public class TriggerParser {

	public Evaluable callback = null;
	public List<Object> returnVal = new ArrayList<>();

	public Integer ch;
	public Integer id;
	public String msgType;
	public String rootType;

	/* Signal */
	public int sigstartbit;
	public int siglength;
	public String sigendian;
	public String sigtype;
	public double sigfactor;
	public double sigoffset;

	/* Condition */
	public String conditionformula;
	public double conditionvalue;
	public double conditionduration;

	public EventCallback eventCallback;
	public boolean dmFlag = false;
	public int tpdtID = 0;
	public String lamp = "";

	public Element mNode;
	
	/* following variable can be changed at runtime */
	public boolean status = false;
	public double time;
	public int lastLength = 0;

	// [DataChannel, DeltaTime, MSGInfo, DataID, DLC, data[10:10 + dlc_Size[DLC]], temp, self.BaseTime, self.VehicleKey]
	// temp = dlcsize + data[10:10+dlcSize[DLC]]

	public Double lastvalue = null;
	public double conditionTime = 0.0;

	// [DataChannel, DeltaTime, MSGInfo, DataID, DLC, data[10:10 + dlc_Size[DLC]], temp, self.BaseTime, self.VehicleKey]
	// temp = dlcsize + data[10:10+dlcSize[DLC]]

	public int lastDmSize = 0;
	public int nowDmSize = 0;
	public byte[] dm1Data;
	public byte[] lastDm1=null;

	public TriggerParser(Element node, EventCallback parent) {
		this.mNode = node;
		this.eventCallback = parent;
		
		status = false;
		callback = null;
		returnVal.clear();
		
		NodeList nodeList = mNode.getChildNodes();
		Optional<Element> msgxml = Parseable.GetElement.apply(nodeList, "Message");
		msgxml.ifPresent(e -> {
			ch = Integer.parseInt(e.getAttribute("Channel"));
			msgType = e.getAttribute("type");
			if(msgType.equals("E")) {
				id = (int)Long.parseLong(e.getAttribute("ID").substring(2/*0x*/), 16) & 0x00FFFFFF;
			}else {
				id = (int)Long.parseLong(e.getAttribute("ID").substring(2/*0x*/), 16) & 0x7FF;
			}
		});
		
		rootType = mNode.getAttribute("type");
		
		if(rootType.equals("CAN")) {
			Optional<Element> sigxml = PolicyParser.GetElement.apply(nodeList, "Siganl");
			sigxml.ifPresent(e -> {
				sigstartbit = Integer.parseInt(e.getAttribute("Startbit"));
				siglength = Integer.parseInt(e.getAttribute("Length"));
				sigendian = e.getAttribute("endian");
				sigtype = e.getAttribute("type");
				sigfactor = Double.parseDouble(e.getAttribute("factor"));
				sigoffset = Double.parseDouble(e.getAttribute("offset"));
			});

			Optional<Element> conditionxml = Parseable.GetElement.apply(nodeList, "condition");
			conditionxml.ifPresent(e -> {
				conditionformula = e.getAttribute("compare");
				conditionvalue = Double.parseDouble(e.getAttribute("value"));
				conditionduration = Double.parseDouble(e.getAttribute("duration"));
			});
			
			callback = new CanEvaluable(this);
			returnVal = Arrays.asList(ch,id,callback);
		}else if(rootType.equals("UDS")) {
			callback = new UdsEvaluable(this);
			lastLength = 0;
			returnVal = Arrays.asList(ch,id,callback);
		}else if(rootType.equals("DM1")) {
			callback = new Dm1Evaluable(this);
			lastDmSize = 0;
			nowDmSize = 0;
			lastDm1 = null;
			dm1Data = null;
			dmFlag = false;
			
			Optional<Element> msgxml2 = Parseable.GetElement.apply(mNode.getChildNodes(), "TPDT");
			msgxml2.ifPresent(e -> {
				if("E".equals(msgType)) {
					tpdtID = (int)Long.parseLong(e.getAttribute("ID").substring(2/*0x*/), 16) & 0x00FFFFFF;
				}else {
					tpdtID = (int)Long.parseLong(e.getAttribute("ID").substring(2/*0x*/), 16) & 0x7FF;
				}
				lamp = e.getAttribute("Lamp");
			});
			returnVal = Arrays.asList(ch,id,callback,tpdtID);
		}
	}

	public EventParser getEventParser() {
		if((eventCallback) != null && eventCallback instanceof EventParser)
			return (EventParser)eventCallback;
		return null;
	}

	public void initData() {
		status = false;

		time = 0;
		lastLength = 0;
		lastvalue = null;
		conditionTime = 0.0;

		lastDmSize = 0;
		nowDmSize = 0;
		dm1Data = null;
		lastDm1=null;

		// initialize Event Node
		Optional.ofNullable(getEventParser()).ifPresent(EventParser::initData);
	}

	public static long GetRawValue(String sigendian, byte[] rawdata, String sigtype, int sigstartbit, int siglength) {
		long rawvalue = 0;
		int startbyte = sigstartbit >> 3;
		int lastbyte = (sigstartbit + siglength -1) >> 3;
		
		if("Little".equalsIgnoreCase(sigendian)) {
			
			for(int i=lastbyte; i > startbyte-1; i--) {
				rawvalue = rawvalue*256 + (rawdata[i]&0xff);
			}
			rawvalue = rawvalue >> (sigstartbit % 8);

		}else {
			for(int i=startbyte; i<lastbyte+1; i++) {
				rawvalue = rawvalue * 256 + (rawdata[i]&0xff);
			}
			rawvalue = rawvalue >> ((8000 - sigstartbit - siglength) % 8);
		}

		rawvalue = rawvalue & (((long)1<< siglength) - 1);
		if("signed".equals(sigtype)) {
			if((rawvalue & (((long)1)<<(siglength - 1))) != 0)
				rawvalue = (-(((~rawvalue) & (((long)1)<<siglength - 1)) + 1));
		}

		return rawvalue;
	}

	public static int GetValue(String sigendian, byte[] rawdata, String sigtype, int sigstartbit, int siglength) {

		int realLength = siglength / 8 + ((siglength % 8) == 0? 0 : 1);
		int rawvalue = 0;
		for(int i= 0; i < siglength; i++) {

			int backwardIndex = (sigstartbit + siglength -1) - i;
			int byteIndex = backwardIndex >> 3;
			int bitshift= 7- backwardIndex%8;
			rawvalue |= (((((0x1 << bitshift) & rawdata[byteIndex]) & 0xff) == 0x00)? 0x00 : 0x01) << i;
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt((int)rawvalue).rewind();
		byte[] rawbytes = byteBuffer.array();

		ByteBuffer resultBuffer = ByteBuffer.allocate(4).clear().order(ByteOrder.BIG_ENDIAN);
		int putStartIndex = ("Little".equals(sigendian))? 0 : 4-realLength;
		IntStream.range(0, realLength).forEach(i-> {
			resultBuffer.put(putStartIndex+i, (byte)rawbytes[4-realLength+i]);
		});
		
		int realvalue = 0;
		if("Little".equals(sigendian)) {
			realvalue = resultBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
		}else {
			realvalue = resultBuffer.order(ByteOrder.BIG_ENDIAN).getInt();
		}
		
		if("signed".equals(sigtype)) {
			if((realvalue & (((int)1)<<(siglength - 1))) != 0)
				realvalue = (-(((~realvalue) & ((((int)1)<<siglength) - 1)) + 1));
		}

//		System.out.printf("\nfinal:sigendian(%s), rawdata[%s],sigtype(%s), start(%d), length(%d),  %d, 0x%x", 
//				sigendian, StringUtils.convertbytesToHex(rawdata, 0, rawdata.length), sigtype, sigstartbit, siglength, realvalue, realvalue);
		return realvalue;
	}

				
	@SuppressWarnings("unused")
	public static Object EvalCAN(TriggerParser trigger, Tuple canmsg) {
		try {
			byte[] rawdata = Base64.decode(canmsg.getString(RawDataField.DATA.getIndex()));
			long baseTime = canmsg.getLong(RawDataField.BaseTime.getIndex());
			long rawvalue = 0;
			int startbyte = trigger.sigstartbit >> 3;
			int lastbyte = (trigger.sigstartbit + trigger.siglength -1) >> 3;
			
			trigger.time = canmsg.getDouble("DeltaTime");

			rawvalue = GetRawValue(trigger.sigendian, rawdata, trigger.sigtype, trigger.sigstartbit, trigger.siglength); 

			/**
			int rawvalue2 = GetValue(trigger.sigendian, rawdata, trigger.sigtype, trigger.sigstartbit, trigger.siglength);
			System.out.printf("\nfinal:rawvalue(%d)!=rawvalue(%d)    sigendian(%s), rawdata[%s],sigtype(%s), start(%d), length(%d)", rawvalue, rawvalue2,
				trigger.sigendian, StringUtils.convertbytesToHex(rawdata, 0, rawdata.length), trigger.sigtype, trigger.sigstartbit, trigger.siglength);
			 * 
			 */

			double value = rawvalue * trigger.sigfactor + trigger.sigoffset;
			boolean rvalue = false;

			switch(trigger.conditionformula) {
				case "GE": {
					if(value >= trigger.conditionvalue)
						rvalue = true;
					break;
				}
				case "GT": {
					if(value > trigger.conditionvalue)
						rvalue = true;
					break;
				}
				case "LE": {
					if(value <= trigger.conditionvalue)
						rvalue = true;
					break;
				}
				case "LT": {
					if(value < trigger.conditionvalue)
						rvalue = true;
					break;
				}
				case "EQ": {
					if(value == trigger.conditionvalue) 
						rvalue = true;
					break;
				}
				case "NEQ": {
					if(value != trigger.conditionvalue)
						rvalue = true;
					break;
				}
				case "DIFF": {
					if(trigger.lastvalue == null) {
						trigger.lastvalue = value;
					}else if(value != trigger.lastvalue) {
						trigger.status = true;
						trigger.lastvalue = value;
						return trigger.eventCallback.OnCalled(trigger.time, true, String.valueOf(value));
					}
					return trigger.eventCallback.OnCalled(trigger.time, null, String.valueOf(value));
				}
			}
			
			if(trigger.status != rvalue) {
				if(trigger.conditionTime + trigger.conditionduration <= trigger.time + baseTime) {
					trigger.status = rvalue;
					trigger.conditionTime = trigger.time + baseTime;
					if(!"DIFF".equals(trigger.conditionformula)) {
						//fullipsori check second parm (trigger.time)
						return trigger.eventCallback.OnCalled(trigger.time, false, String.valueOf(value));
					}
				}
			}else {
				trigger.status = rvalue;
				if(trigger.conditionTime == 0) {
					trigger.conditionTime = trigger.time + baseTime;
					if(!"DIFF".equals(trigger.conditionformula)) {
						return trigger.eventCallback.OnCalled(trigger.time, false, String.valueOf(value));
					}
				}
				trigger.conditionTime = trigger.time + baseTime;
			}
			return trigger.eventCallback.OnCalled(trigger.time, null, String.valueOf(value));

		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
			return null;
		}
	}
	
	public static Object EvalDM1(TriggerParser trigger, Tuple dm1msg) {
		try {
			byte[] rawdata = Base64.decode(dm1msg.getString(RawDataField.DATA.getIndex()));
			int dataID= dm1msg.getInt(RawDataField.DataID.getIndex());
			trigger.time = dm1msg.getDouble(RawDataField.DeltaTime.getIndex());
			
			if(trigger.id == (dataID & 0x00FFFFFF)) {
				if((rawdata[5]&0xFF) == 0xCA && (rawdata[6]&0xFF) == 0xFE) {
					trigger.nowDmSize = (rawdata[2] & 0xF) * 256 + (rawdata[1]&0xFF);
					trigger.dm1Data = rawdata;
				}else {
					trigger.dm1Data = null;
				}
			}else if(trigger.tpdtID == (dataID & 0x00FFFFFF) && trigger.lamp.equals("False")) {
				if(trigger.dm1Data != null && rawdata[0] == 1) {
					if(trigger.lastDmSize == 0) {
						trigger.lastDmSize = trigger.nowDmSize;
					}else {
						if(trigger.nowDmSize != trigger.lastDmSize) {
							trigger.status = true;
							trigger.lastDmSize = trigger.nowDmSize;
							trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
							trigger.dm1Data = null;
							return trigger.eventCallback.OnCalled(trigger.time, true, null); 
						}
					}
					
					if(trigger.lastDm1 == null) {
						trigger.status = true;
						trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
						trigger.dm1Data = null;
						return trigger.eventCallback.OnCalled(trigger.time, true, null);
					}else {
						if(!Arrays.equals(trigger.lastDm1, StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length)))) {
							trigger.status = true;
							trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
							trigger.dm1Data = null;
							return trigger.eventCallback.OnCalled(trigger.time, true, null);
						}
					}
				}
			}else if(trigger.tpdtID == (dataID & 0x00FFFFFF) && "AWL".equals(trigger.lamp)) {
				if(trigger.dm1Data != null && rawdata[0] == 1) {
					boolean rvalue = false;
					int awlStatus = (rawdata[1] & (byte)0B00001100) >> 2;
					if(awlStatus == 1) {
						rvalue = true;
					}
					trigger.status = rvalue;
					return trigger.eventCallback.OnCalled(trigger.time, false, null);
				}
			}else if(trigger.tpdtID == (dataID & 0x00FFFFFF) && "RSL".equals(trigger.lamp)) {
				if(trigger.dm1Data != null && (rawdata[0]&0xff) == 1) {
					boolean rvalue = false;
					int awlStatus = (rawdata[1] & 0B00110000) >> 4;
					if(awlStatus == 1) {
						rvalue = true;
					}
					trigger.status = rvalue;
					return trigger.eventCallback.OnCalled(trigger.time, false, null);
				}
			}
			return trigger.eventCallback.OnCalled(trigger.time, null, null);

		}catch(Exception e) {
			System.out.println("Exception:" + e.getMessage());
			return null;
		}
	}

	public static Object EvalUDS(TriggerParser trigger, Tuple udsmsg) {
		try {
			byte[] rawdata = Base64.decode(udsmsg.getString(RawDataField.DATA.getIndex()));
			int frameType = (rawdata[0] & 0xff) >> 4;;
			int length;

			trigger.time = udsmsg.getDouble("DeltaTime");
			
			if(frameType == 0) {
				length = rawdata[0] & 0xF;
				if((rawdata[1]&0xFF) == 0x59 || (rawdata[1]&0xFF) == 0x58) {
					if(trigger.lastLength == 0) {
						trigger.lastLength = length;
						if(length > 3) {
							trigger.status = true;
							return trigger.eventCallback.OnCalled(trigger.time, true, null);
						}
					}else {
						if(trigger.lastLength != length && length > 3) {
							trigger.status = true;
							trigger.lastLength = length;
							return trigger.eventCallback.OnCalled(trigger.time, true, null);
						}else if(trigger.lastLength != length && length <= 3) {
							trigger.status = false;
							trigger.lastLength = length;
							return trigger.eventCallback.OnCalled(trigger.time, true, null);
						}
					}
				}
			}else if(frameType == 1) {
				length = (rawdata[0] & 0xF) * 256 + (rawdata[1] & 0xFF);
				if((rawdata[2]&0xFF) == 0x59 || (rawdata[2]&0xFF) == 0x58) {
					if(trigger.lastLength != length) {
						trigger.status = true;
						trigger.lastLength = length;
						return trigger.eventCallback.OnCalled(trigger.time, true, null);
					}
				}
			}

			return trigger.eventCallback.OnCalled(trigger.time, null, null);

		}catch(Exception e) {
			System.out.println("Exception:" + e.getMessage());
			return null;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//00ffde6cf9ffffff
//		byte[] x = {(byte)0xf7,0x00,0x00,0x43,0x53,(byte)0xff,(byte)0xff,(byte)0xfa};
		byte[] x = {(byte)0x00,(byte)0xff,(byte)0xde,(byte)0x6c,(byte)0xf9,(byte)0xff,(byte)0xff,(byte)0xff};
		long rawvalue = TriggerParser.GetRawValue("Little", x, "unsigned", 16, 24);
		System.out.println("rawvalue:" + rawvalue);
//		if(rawdata[5] == (byte)0xCA && rawdata[6] == (byte)0xFE) {
//
//		}
	}
}