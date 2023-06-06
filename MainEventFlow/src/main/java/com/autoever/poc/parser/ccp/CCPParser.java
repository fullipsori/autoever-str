package com.autoever.poc.parser.ccp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.autoever.poc.parser.can.TriggerParser;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Pair;

public class CCPParser {

	// return Tuple data
	int prevCmd = 0;
	
	public CCPParser() {}
	
	public void initParam() {
		prevCmd = 0;
	}
	
	public List<Pair<String, String>> GetTuple(String terminalId, byte[] rawdata) {
		if(prevCmd != 0 ) {
			if(prevCmd != 255 && ((rawdata[0]&0xff) >= 0x0a) && ((rawdata[0]&0xff) <= 0x3b)) {
				prevCmd = rawdata[0] & 0xff;
				return parseData(rawdata, EVTRepository.getInstance().mODTMap.get(terminalId));
			}
		}
		prevCmd = rawdata[0] & 0xff;
		return null;
	}
	
	private List<Pair<String, Long>> parseData(byte[] rawdata, EVTParser evtParser) {
		int cmd = rawdata[0] & 0xff;
		Object[] odt = evtParser.odt_map.get(cmd-10);
		if(((List<?>)odt[0]).isEmpty()) return null;
		List<Pair<String,Long>> resultList = new ArrayList<>();
		int index = 1;
		for(int i=0; i<((String)odt[2]).length(); i++) {
			switch(((String)odt[2]).charAt(i)) {
				case 'L' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							Long.parseLong(Arrays.copyOfRange(rawdata, index, 4), 0, 4)));
					index += 4;
					break;
				case 'H' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							Long.parseLong(Arrays.copyOfRange(rawdata, index, 2), 0, 2)));
					index += 2;
					break;
				case 'B' :
					resultList.add(new Pair<String,Long>(
							(String)((List<String>)odt[0]).get(i), 
							Long.parseLong(Arrays.copyOfRange(rawdata, index, 1), 0, 1)));
					index += 1;
					break;
			}
		}
		
		return resultList;
	}

	public static Object[] unpack(byte[] data, String format) {
		  ByteBuffer buffer = ByteBuffer.wrap(data);
		  Object[] values = new Object[format.length()];
		  for (int i = 0; i < format.length(); i++) {
		    switch (format.charAt(i)) {
		      case 'L':
		        values[i] = buffer.getInt();
		        break;
		      case 'H':
		        values[i] = buffer.getLong();
		        break;
		      case 'B':
		        values[i] = buffer.get();
		        break;
		      default:
		        throw new IllegalArgumentException("Invalid format character: " + format.charAt(i));
		    }
		  }
		  return values;
		}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
