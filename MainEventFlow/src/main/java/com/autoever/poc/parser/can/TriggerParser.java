package com.autoever.poc.parser.can;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.autoever.poc.common.StringUtils;
import com.autoever.poc.parser.Parseable;

public class TriggerParser {

	public boolean status = false;
	public BiFunction<TriggerParser,List<String>, Object> callback = null;
	public List<Object> returnVal = new ArrayList<>();

	public Integer ch;
	public Integer id;
	public String msgType;
	public String type;

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

	/* parse */
	public long time;
	public int lastLength = 0;

	public EventCallback eventCallback;

	private Element mNode;
	

	public TriggerParser(Element node, EventCallback cb) {
		this.mNode = node;
		this.eventCallback = cb;
		
		status = false;
		callback = null;
		returnVal.clear();
		
		NodeList nodeList = mNode.getChildNodes();
		Optional<Element> msgxml = Parseable.GetElement.apply(nodeList, "Message");
		msgxml.ifPresent(e -> {
			ch = Integer.parseInt(e.getAttribute("Channel"));
			id = Integer.parseInt(e.getAttribute("ID").substring(2/*0x*/), 16);
			msgType = e.getAttribute("type");
		});
		
		if(msgType.equals("E")) {
			id &= 0x00FFFFFF;
		}else {
			id &= 0x7FF;
		}
		type = mNode.getAttribute("type");
		
		if(type.equals("CAN")) {
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
			
			callback = canParseable;
			returnVal = Arrays.asList(ch,id,callback);
		}else if(type.equals("UDS")) {
			callback = udsParseable;
			lastLength = 0;
			returnVal = Arrays.asList(ch,id,callback);
		}else if(type.equals("DM1")) {
			callback = dmParseable;
			lastDmSize = 0;
			nowDmSize = 0;
			lastDm1 = null;
			dm1Data = null;
			dmFlag = false;
			
			Optional<Element> msgxml2 = Parseable.GetElement.apply(mNode.getChildNodes(), "TPDT");
			msgxml2.ifPresent(e -> {
				tpdtID = Integer.parseInt(e.getAttribute("ID").substring(2/*0x*/), 16);
				if("E".equals(msgType)) {
					tpdtID &= 0x00FFFFFF;
				}else {
					tpdtID &= 0x7FF;
				}
				lamp = e.getAttribute("Lamp");
			});
			returnVal = Arrays.asList(ch,id,callback,tpdtID);
		}
	}

	// [DataChannel, DeltaTime, MSGInfo, DataID, DLC, data[10:10 + dlc_Size[DLC]], temp, self.BaseTime, self.VehicleKey]
	// temp = dlcsize + data[10:10+dlcSize[DLC]]

	public Double lastvalue = null;
	public double conditionTime = 0.0;

	// [DataChannel, DeltaTime, MSGInfo, DataID, DLC, data[10:10 + dlc_Size[DLC]], temp, self.BaseTime, self.VehicleKey]
	// temp = dlcsize + data[10:10+dlcSize[DLC]]

	public int lastDmSize = 0;
	public int nowDmSize;
	public byte[] dm1Data;
	public boolean dmFlag = false;
	public int tpdtID = 0;
	public byte[] lastDm1=null;
	public String lamp = "";


	public static BiFunction<TriggerParser, List<String>, Object> canParseable = (trigger, canmsg) -> {
		byte[] rawdata = canmsg.get(5).getBytes();
		
		trigger.time = Long.parseLong(canmsg.get(1));
		long baseTime = Long.parseLong(canmsg.get(7));
		int rawvalue = 0;
		int startbyte = trigger.sigstartbit >> 3;
		int lastbyte = (trigger.sigstartbit + trigger.siglength -1) >> 3;
		
		ByteBuffer byteBuffer;

		/** fullipsori: java 에서 동작하는지 확인 필요함 **/
		if("Little".equals(trigger.sigendian)) {
			
			for(int i=lastbyte; i > startbyte-1; i--) {
				rawvalue = rawvalue*256 + rawdata[i];
			}
			rawvalue = rawvalue >> (trigger.sigstartbit % 8);
			/**
			byteBuffer = ByteBuffer.wrap(rawdata, startbyte, lastbyte-startbyte+1).order(ByteOrder.LITTLE_ENDIAN);
			rawvalue = byteBuffer.getInt();
			rawvalue = rawvalue>> (trigger.sigstartbit % 8);
			**/
		}else {
			
			for(int i=startbyte; i<lastbyte+1; i++) {
				rawvalue = rawvalue * 256 + rawdata[i];
			}
			rawvalue = rawvalue >> ((8000 - trigger.sigstartbit - trigger.siglength) % 8);
			/**
			byteBuffer = ByteBuffer.wrap(rawdata, startbyte, lastbyte-startbyte+1).order(ByteOrder.BIG_ENDIAN);
			rawvalue = byteBuffer.getInt();
			rawvalue = rawvalue>> ((8000 - trigger.sigstartbit - trigger.siglength) % 8);
			**/
		}
		/*
		rawvalue = rawvalue & ((int)Math.pow(2,trigger.siglength) -1);
		if("signed".equals(trigger.sigtype)) {
			if((rawvalue & (1<<(trigger.siglength-1))) != 0) {
				rawvalue = (-(((~rawvalue) & ((int)Math.pow(2, trigger.siglength) -1)) + 1));
			}
		}
		*/
		rawvalue = rawvalue & (((int)1<< trigger.siglength) - 1);
		if("signed".equals(trigger.sigtype)) {
			if((rawvalue & (((int)1)<<(trigger.siglength - 1))) != 0)
				rawvalue = (-(((~rawvalue) & ((((int)1)<<trigger.siglength) - 1)) + 1));
		}

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
					return trigger.eventCallback.OnCalled(trigger.time, true);
				}
				return null;
			}
		}
		
		if(trigger.status != rvalue) {
			if(trigger.conditionTime + trigger.conditionduration <= trigger.time + baseTime) {
				trigger.status = rvalue;
				trigger.conditionTime = trigger.time + baseTime;
				if(!"DIFF".equals(trigger.conditionformula)) {
					//fullipsori check second parm (trigger.time)
					return trigger.eventCallback.OnCalled(trigger.time, false);
				}
			}
		}else {
			trigger.status = rvalue;
			if(trigger.conditionTime == 0) {
				trigger.conditionTime = trigger.time + baseTime;
				if(!"DIFF".equals(trigger.conditionformula)) {
					return trigger.eventCallback.OnCalled(trigger.time, false);
				}
			}
		}

		return null;
	};
	
	public static BiFunction<TriggerParser, List<String>, Object> dmParseable = (trigger, dm1msg) -> {
		byte[] rawdata = dm1msg.get(5).getBytes(); //fullipsori "ISO8859-1"
		trigger.time = Long.parseLong(dm1msg.get(1));

		if(trigger.id == (Integer.parseInt(dm1msg.get(3)) & 0x00FFFFFF)) {
			if(rawdata[5] == 0xCA && rawdata[6] == 0xFE) {
				trigger.nowDmSize = (rawdata[2] & 0xF) * 256 + rawdata[1];
				trigger.dm1Data = rawdata;
			}else {
				trigger.dm1Data = null;
			}
		}else if(trigger.tpdtID == (Integer.parseInt(dm1msg.get(3)) & 0x00FFFFFF) && trigger.lamp.equals("False")) {
			if(trigger.dm1Data != null && rawdata[0] == 1) {
				if(trigger.lastDmSize == 0) {
					trigger.lastDmSize = trigger.nowDmSize;
				}else {
					if(trigger.nowDmSize != trigger.lastDmSize) {
						trigger.status = true;
						trigger.lastDmSize = trigger.nowDmSize;
						trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
						trigger.dm1Data = null;
						return trigger.eventCallback.OnCalled(trigger.time, true);
					}
				}
				
				if(trigger.lastDm1 == null) {
					trigger.status = true;
					trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
					trigger.dm1Data = null;
					return trigger.eventCallback.OnCalled(trigger.time, true);
				}else {
					if(Arrays.equals(trigger.lastDm1, StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length)))) {
						trigger.status = true;
						trigger.lastDm1 = StringUtils.mergeByteArray(Arrays.copyOfRange(rawdata, 3, 6), Arrays.copyOfRange(rawdata, 7, rawdata.length));
						trigger.dm1Data = null;
						return trigger.eventCallback.OnCalled(trigger.time, true);
					}
				}
			}
		}else if(trigger.tpdtID == (Integer.parseInt(dm1msg.get(3)) & 0x00FFFFFF) && "AWL".equals(trigger.lamp)) {
			if(trigger.dm1Data != null && rawdata[0] == 1) {
				boolean rvalue = false;
				//fullipsori bit 확인
				int awlStatus = (rawdata[1] & 0B00001100) >> 2;
				if(awlStatus == 1) {
					rvalue = true;
				}
				trigger.status = rvalue;
				return trigger.eventCallback.OnCalled(trigger.time, false);
			}
		}else if(trigger.tpdtID == (Integer.parseInt(dm1msg.get(3)) & 0x00FFFFFF) && "RSL".equals(trigger.lamp)) {
			if(trigger.dm1Data != null && rawdata[0] == 1) {
				boolean rvalue = false;
				int awlStatus = (rawdata[1] & 0B00110000) >> 4;
				if(awlStatus == 1) {
					rvalue = true;
				}
				trigger.status = rvalue;
				return trigger.eventCallback.OnCalled(trigger.time, false);
			}
		}
		return null;
	};

	public static BiFunction<TriggerParser, List<String>, Object> udsParseable = (trigger, udsmsg) -> {
		byte[] rawdata = udsmsg.get(5).getBytes(); //fullipsori "ISO8859-1"
		
		trigger.time = Long.parseLong(udsmsg.get(1));
		int frameType = ((int)rawdata[0]) >> 4;;
		int length;
		
		if(frameType == 0) {
			length = rawdata[0] & 0xF;
			if(rawdata[1] == 0x59 || rawdata[1] == 0x58) {
				if(trigger.lastLength == 0) {
					trigger.lastLength = length;
					if(length > 3) {
						trigger.status = true;
						return trigger.eventCallback.OnCalled(trigger.time, true);
					}
				}else {
					if(trigger.lastLength != length && length > 3) {
						trigger.status = true;
						trigger.lastLength = length;
						return trigger.eventCallback.OnCalled(trigger.time, true);
					}else if(trigger.lastLength != length && length <= 3) {
						trigger.status = false;
						trigger.lastLength = length;
						return trigger.eventCallback.OnCalled(trigger.time, true);
					}
				}
			}
		}else if(frameType == 1) {
			length = (rawdata[0] & 0xF) * 256 + rawdata[1];
			if(rawdata[2] == 0x59 || rawdata[2] == 0x58) {
				if(trigger.lastLength != length) {
					trigger.status = true;
					trigger.lastLength = length;
					return trigger.eventCallback.OnCalled(trigger.time, true);
				}
			}
		}

		return null; //check null or False
	};
	
	public static void  endian(String sigendian, byte[] rawdata, String sigtype, int sigstartbit, int siglength) {

		int startbyte = sigstartbit >> 3;
		int lastbyte = (sigstartbit + siglength -1) >> 3;
		int lengthbyte = lastbyte - startbyte + 1;
		int sigendbit = (sigstartbit+siglength)%8;
		int finallength = siglength / 8 + ((siglength % 8) == 0? 0 : 1);
		
		BigInteger bigInt = new BigInteger(Arrays.copyOfRange(rawdata, startbyte, lastbyte+1));
		byte[] shifted_1 = bigInt.shiftRight( (8 - sigendbit)%8 ).shiftLeft((8 - sigendbit)%8).toByteArray();
		IntStream.range(0, shifted_1.length).forEach(i -> System.out.printf("0x%x ", shifted_1[i]));
		System.out.println("\n");

		byte[] shifted = bigInt.shiftLeft( sigstartbit%8 ).toByteArray();
		IntStream.range(0, shifted.length).forEach(i -> System.out.printf("0x%x ", shifted[i]));
		System.out.println("\n");
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(4).clear();
		int x_startbyte = ("Little".equals(sigendian))? 0 : 4-finallength;
		IntStream.range(0, finallength).forEach(i-> byteBuffer.put(x_startbyte+i, shifted[i]));
		byteBuffer.rewind();
		
		System.out.println("");
		int rawvalue = 0;
		if("Little".equals(sigendian)) {
			rawvalue = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
			System.out.printf("0x%x", rawvalue);
		}else {
			rawvalue = byteBuffer.order(ByteOrder.BIG_ENDIAN).getInt();
			System.out.printf("0x%x", rawvalue);
		}
	
		if("signed".equals(sigtype)) {
			if((rawvalue & (((int)1)<<(siglength - 1))) != 0)
				rawvalue = (-(((~rawvalue) & ((((int)1)<<siglength) - 1)) + 1));
		}
		
		System.out.printf("\nfinal:%d, 0x%x", rawvalue, rawvalue);
	}

	//fullipsori
	public static void  endian2(String sigendian, byte[] rawdata, String sigtype, int sigstartbit, int siglength) {

		int startbyte = sigstartbit >> 3;
		int lastbyte = (sigstartbit + siglength -1) >> 3;
		int lengthbyte = lastbyte - startbyte + 1;
		
		int sigendbit = (sigstartbit+siglength)%8;
		int finallength = siglength / 8 + ((siglength % 8) == 0? 0 : 1);
		
		/** second **/
		long rawvalue = 0;
		for(int i=startbyte; i<=lastbyte; i++) {
			if(i==startbyte) {
				rawvalue += (0xFFFFFFFF & ((rawdata[i] << (sigstartbit%8))>>(sigstartbit%8))) << 8*(lastbyte-i);
			}
			if(i==lastbyte) {
				rawvalue += (0xFFFFFFFF & (rawdata[i] >> sigendbit)) << sigendbit;
			}
			if(i != startbyte && i != lastbyte)
			{
				rawvalue += (0xFFFFFFFF & rawdata[i]) << 8*(lastbyte-i);
			}
		}
		
		rawvalue <<= (sigstartbit%8);
		int shiftright = lengthbyte - finallength;
		rawvalue >>= shiftright*8;

		System.out.printf("rawvalue:0x%x, %d,%d\n", rawvalue, startbyte, lastbyte);
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt((int)rawvalue).rewind();
		byte[] shifted = byteBuffer.array();
		IntStream.range(0, shifted.length).forEach(i -> System.out.printf("0x%x ", shifted[i]));

		ByteBuffer endianBuffer = ByteBuffer.allocate(4).clear().order(ByteOrder.BIG_ENDIAN);
		int x_startbyte = ("Little".equals(sigendian))? 0 : 4-finallength;
		IntStream.range(0, finallength).forEach(i-> {
			System.out.println("\ninsert:" + i  + " " +  (byte)shifted[4-finallength+i]);
			endianBuffer.put(x_startbyte+i, (byte)shifted[4-finallength+i]);
		});
		
		System.out.println("");
		int realvalue = 0;
		if("Little".equals(sigendian)) {
			realvalue = endianBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
			System.out.printf("0x%x", realvalue);
		}else {
			realvalue = endianBuffer.order(ByteOrder.BIG_ENDIAN).getInt();
			System.out.printf("0x%x", realvalue);
		}
		
		if("signed".equals(sigtype)) {
			if((realvalue & (((int)1)<<(siglength - 1))) != 0)
				realvalue = (-(((~realvalue) & ((((int)1)<<siglength) - 1)) + 1));
		}

		System.out.printf("\nfinal:%d, 0x%x", realvalue, realvalue);

	}

	public static void  endian3(String sigendian, byte[] rawdata, String sigtype, int sigstartbit, int siglength) {

		int realLength = siglength / 8 + ((siglength % 8) == 0? 0 : 1);
		int rawvalue = 0;
		for(int i= 0; i < siglength; i++) {

			int backwardIndex = (sigstartbit + siglength -1) - i;
			int byteIndex = backwardIndex >> 3;
			int bitshift= 7- backwardIndex%8;
			rawvalue |= ((((byte)((0x1 << bitshift) & rawdata[byteIndex]) & (byte)0xff) == 0x00)? 0x00 : 0x01) << i;
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

		System.out.printf("\nfinal:%d, 0x%x", realvalue, realvalue);

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		byte[] x = {0x11, 0x12, 0x13,0x04};
//		TriggerParser.endian("Big", x, "unsigned", 3, 16);
		System.out.println("\n\n");
		TriggerParser.endian3("Little", x, "signed", 3, 16);
		

//		byte[] x = {0x08, 0x10, 0x18,0x00};
//		ByteBuffer byteBuffer = ByteBuffer.wrap(x);
//		int xx = byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
//		System.out.printf("Result:%d:0x%x" , xx, xx);
		/*
		long rawvalue = 0;
		
		byte[] x_p = {0x01, 0x02,0x03,0x04};
		int mask = 0xFFFFFFFF << 3;
		
		// Little
		for(int i=1; i > -1; i--) {
			rawvalue = rawvalue*256 + x_p[i];
		}
//		rawvalue = rawvalue >> ( 3% 8);
//		rawvalue = rawvalue & ((int)Math.pow(2, 16) -1);
		rawvalue = rawvalue & (((int)1<<16) - 1);
//		rawvalue = rawvalue & mask;
		if((rawvalue & (1<<(16-1))) != 0) {
			rawvalue = (-(((~rawvalue) & (((int)1)<<16 - 1)) + 1));
		}
		System.out.printf("rawvalue:0x%x\n" , rawvalue);
		
//		ByteBuffer byteBuffer = ByteBuffer.wrap(x, 0, 2).order(ByteOrder.LITTLE_ENDIAN);
//		System.out.println("little:" + byteBuffer.order(ByteOrder.LITTLE_ENDIAN).get(0) + " " 
//					+ byteBuffer.order(ByteOrder.LITTLE_ENDIAN).get(1) + " "
//					+ byteBuffer.order(ByteOrder.LITTLE_ENDIAN).get(2) + " "
//					+ byteBuffer.order(ByteOrder.LITTLE_ENDIAN).get(3) + " "
//					+ "result:" + Integer.toHexString(byteBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt())
//				);

//		int avalue = byteBuffer.getShort();
//		avalue = (avalue << (3%8));
//		avalue = avalue & (((int)1<<16) - 1);
//		if((avalue & (((int)1)<<(16-1))) != 0)
//			avalue = (-(((~avalue) & (((int)1)<<16 - 1)) + 1));
//		System.out.printf("avalue:0x%x\n" , avalue);


		// Big
		rawvalue = 0;
		for(int i=0; i<2; i++) {
			rawvalue = rawvalue * 256 + x_p[i];
		}
		rawvalue = rawvalue >> ((8000 - 3 - 16) % 8);
		rawvalue = rawvalue & (((int)1<<16) - 1);
//		rawvalue = rawvalue & mask;
		if((rawvalue & (1<<(16-1))) != 0) {
			rawvalue = (-(((~rawvalue) & (((int)1)<<16 - 1)) + 1));
		}
		System.out.printf("rawvalue:0x%x\n" , rawvalue);
		
//		avalue = 0;
//		
//		byteBuffer = ByteBuffer.wrap(x).order(ByteOrder.BIG_ENDIAN);
//
//		avalue = byteBuffer.getInt();
//		avalue = avalue << ((8000 - 3 - 16) % 8);
//		avalue = avalue & ((((int)1)<<16) -1);
//		if((avalue & (((int)1)<<(16-1))) != 0)
//			avalue = (-(((~avalue) & (((int)1)<<16 - 1)) + 1));
//
//		System.out.printf("avalue:0x%x" , avalue);
		
		*/
		
		
	}
}