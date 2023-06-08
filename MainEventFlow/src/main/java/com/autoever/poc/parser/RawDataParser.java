package com.autoever.poc.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.autoever.poc.common.NumUtils;
import com.autoever.poc.parser.can.PolicyParser;
import com.autoever.poc.parser.can.PolicyRepository;
import com.streambase.sb.CompleteDataType;
import com.streambase.sb.NullValueException;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;
import com.streambase.sb.TupleException;
import com.streambase.sb.util.Base64;

public class RawDataParser implements Parseable {

	private byte[] hcpMessage;
	private List<Tuple> parsedData = new ArrayList<>();
	final static Schema dataSchema = new Schema(
				"RAWDATA",
					new Schema.Field("DataChannel", CompleteDataType.forInt()),
					new Schema.Field("DeltaTime", CompleteDataType.forDouble()),
					new Schema.Field("MSGInfo", CompleteDataType.forInt()),
					new Schema.Field("DataID", CompleteDataType.forInt()),
					new Schema.Field("DLC", CompleteDataType.forInt()),
					new Schema.Field("DATA", CompleteDataType.forString())
					, new Schema.Field("BaseTime", CompleteDataType.forLong())
				);

	public RawDataParser(byte[] message) {
		this.hcpMessage = message;
	}
	
	public RawDataParser(String filePath) {
		byte[] allBytes;
		try {
			allBytes = Files.readAllBytes(Paths.get(filePath));
			if(allBytes != null && allBytes.length > 0) {
				this.hcpMessage = allBytes;
			} else {
				this.hcpMessage = null;
			}
		} catch (IOException e) { }
	}
	

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		int[] dlcSize = {0,1,2,3,4,5,6,7,8,12,16,20,24,32,48,64};
		int sIndex = 0;
		final int headerSize = 10;	//IBBI(10byte)
		
		try {
			int rawcount = hcpMessage.length;
			while(sIndex < rawcount) {
				int dlcIndex = NumUtils.getIntFromBig(hcpMessage, sIndex, 1);
				sIndex += 1;
				int curIndex =  sIndex;
				double deltaTime = (double)(NumUtils.getLongFromBig(hcpMessage, curIndex, 4) * 0.00005);
				curIndex += 4;
				int dataFlag = NumUtils.getIntFromBig(hcpMessage, curIndex, 1);
				curIndex += 1;
				int dataChannel = NumUtils.getIntFromBig(hcpMessage, curIndex, 1);
				curIndex += 1;
				int dataId = NumUtils.getIntFromBig(hcpMessage, curIndex, 4);
				curIndex += 4;
				int dataSize = dlcSize[dlcIndex];
				byte[] rawData = Arrays.copyOfRange(hcpMessage, curIndex, curIndex + dataSize);
				Tuple dataTuple = dataSchema.createTuple();

				dataTuple.setInt(0, dataChannel);
				dataTuple.setDouble(1, deltaTime);
				dataTuple.setInt(2, dataFlag);
				dataTuple.setInt(3, dataId);
				dataTuple.setInt(4, dlcIndex);
				dataTuple.setString(5, Base64.encodeBytes(rawData));
				
				dataTuple.setLong(6, 10000);  //for BaseTime

				parsedData.add(dataTuple);
				sIndex += (dataSize + headerSize);
			}
		}catch(Exception e) {
			System.out.println("RawParser Exception:" + e.getMessage());
			return;
		}
		
		return;
	}

	public List<Tuple> getParsed() {
		if(parsedData.isEmpty()) parse();
		return parsedData;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String filepath= "d:/projects/vdms/resources/download/BM-15C-0088_142714_1_1685938107_1685938583675.dat";
		PolicyRepository.getInstance().LoadPolicy("d:/projects/vdms/resources/policy", "xml");
		String filename =  Paths.get(filepath).getFileName().toString().substring(0, Paths.get(filepath).getFileName().toString().lastIndexOf('.')).split("_")[0];
		// can data test
		
		List<Tuple> result = new RawDataParser(filepath).getParsed();
		
		if(result.isEmpty()) {
			System.out.println("empty");
			return;
		}
		
		PolicyParser.debug_val[0] = result.size();

		PolicyParser parser = PolicyRepository.getInstance().mPolicyMap.get(filename);
		result.stream().forEach(t -> {
			if(parser.GetKeyFlag(t)) {
				List<Tuple> res = parser.GetTrigData(t);
				if(res != null) {
					res.stream().filter(e -> e != null)
					.filter(e -> {
						try {
							return !"NOT".equals(e.getString(5)) && !"RET".equals(e.getString(5));
						} catch (Exception e2) {
							return false;
						}
					})
					.filter(e -> {
						try {
							return !e.getString(5).equals("NOMATCH") && !e.getString(5).equals("RET");
						} catch (Exception e2) {
							return false;
						}
					})
					.forEach(e -> {
						try {
							System.out.println("" + e.getDouble(0) + " " + e.getDouble(1) + " " + e.getDouble(2) + " " + e.getString(3) + " " + e.getString(4) + " " + e.getString(5) + " " + e.getString(6));
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					});
				}
			}
				
		});

		Arrays.stream(PolicyParser.debug_val).forEach(System.out::println);
	}

}
