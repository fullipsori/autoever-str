package com.autoever.poc.parser.can;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.autoever.poc.adapters.VdmsRawParser.RawParserDataField;
import com.autoever.poc.common.RawDataField;
import com.autoever.poc.parser.Parseable;
import com.streambase.sb.Tuple;

public class PolicyParser implements Parseable {

	private String filename;
	private Path xmlFilePath;
	private Element rootNode;

	public String KeyStatus = "ON";
	public TriggerParser KeyTrig;

	private static EventCallback eventCallback = (a,b,c) -> {
		return null;
	};
	
	/* Atomic 변수가 되어야 하는지 체크 필요함. */
	public int rootCount = 0;

	public PolicyParser(Path filePath, DocumentBuilder documentBuilder) {
		this.xmlFilePath = filePath;
		this.filename = this.xmlFilePath.getFileName().toString().substring(0, this.xmlFilePath.getFileName().toString().lastIndexOf('.'));
		try {
			Document document = documentBuilder.parse(xmlFilePath.toFile());
			rootNode = document.getDocumentElement();
		} catch (Exception e) {
			rootNode = null;
		}
		parse();
	}

	public long minPreTime = 0;
				
	public Map<Integer, Map<Integer, List<Evaluable>>> msgFilter;

	public String GetFileName() {
		return this.filename;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse() {
		try {
			if(rootNode == null) return;

			NodeList children = rootNode.getChildNodes();
			GetElement.apply(children, "PreCondition").ifPresent(e -> {
				KeyStatus = e.getAttribute("Key");
				GetElement.apply(e.getChildNodes(), "Trigger").ifPresent(ele -> {
					KeyTrig = new TriggerParser(ele, eventCallback);
				});
			});
			

			msgFilter = GetElements.apply(children, "Event").stream()
					.map(EventParser::new)
					.peek(e -> minPreTime = (minPreTime < e.preTime)? e.preTime : minPreTime)
					.flatMap(e -> e.msgTable.stream())
					.map(ta -> (List<Object>)ta)
					.flatMap(ta -> (ta.size() > 3)?
						Stream.of(
							List.of(ta.get(0), ta.get(1), ta.get(2)), 
							List.of(ta.get(0), ta.get(3), ta.get(2)))
						:
						Stream.of( List.of(ta.get(0), ta.get(1), ta.get(2)))
					)
					.filter(ta -> ta.get(1) != null)
					.collect(Collectors.groupingBy(ta -> (int)ta.get(0), 
								Collectors.groupingBy(ta->(int)ta.get(1), 
									Collectors.mapping(ta -> (Evaluable)ta.get(2), Collectors.toList())  )));


		} catch (Exception e) {
			System.out.println("Parse Exception:" + "filename:" + this.filename +  e.getMessage());
			// TODO Auto-generated catch block
		};
	}

	//[DataChannel, DeltaTime, MSGInfo, DataID, DLC, data[10:10 + dlc_Size[DLC]]]
	public boolean GetKeyFlag(Tuple message) {
		try {
			int dataChannel = message.getInt(RawDataField.DataChannel.getIndex());
			int dataID = message.getInt(RawDataField.DataID.getIndex());
			if((dataID & 0x00FFFFFF) == KeyTrig.id && dataChannel == KeyTrig.ch) {
				KeyTrig.callback.Evaluate(message);
			}
			
			if(!"ON".equalsIgnoreCase(KeyStatus) || KeyTrig.status) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("GetKeyFlag Exception:" + e.getMessage());
			return false;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Tuple> GetTrigData(Tuple message) {

		try {
			int dataChannel = message.getInt(RawDataField.DataChannel.getIndex());
			double deltaTime = message.getDouble(RawDataField.DeltaTime.getIndex());
			int dataID = message.getInt(RawDataField.DataID.getIndex());

			if(!msgFilter.containsKey(dataChannel)) return null;

			List<Evaluable> callbacks = (List<Evaluable>)((Map<Integer, List<Evaluable>>)msgFilter.get(dataChannel)).get(dataID & 0x00FFFFFF);
			if(callbacks != null) {
				// [name, String.valueOf(preTime), String.valueOf(postTime), category, "OnFalse", value]
				// name, String.valueOf(preTime), String.valueOf(postTime), category, "NOT or RET or status", value
				return callbacks.stream()
					.map(c -> (List<String>)c.Evaluate(message))
					.filter(e -> e != null)
					.collect(Collectors.toMap(e-> e.get(0), e->e, (existVal, newVal) -> newVal))
					.values().stream()
					.filter(e -> e != null)
					.map(e -> {
						try {
							Tuple tuple = PolicyRepository.trigDataSchema.createTuple();
							tuple.setDouble(0, (double)(deltaTime-Double.parseDouble(e.get(1))));
							tuple.setDouble(1, (double)(deltaTime+Double.parseDouble(e.get(2))));
							tuple.setDouble(2, (double)deltaTime);
							tuple.setString(3, e.get(0));
							tuple.setString(4, e.get(3));
							tuple.setString(5, e.get(4));
							tuple.setString(6, e.get(5));
							return tuple;
						}catch (Exception x){
							System.out.println("PolicyParser Tuple Gen Excep:" + x.getMessage());
							return null;
						}
					})
					.filter(t -> t != null)
					.collect(Collectors.toList());
			}

			return null;
		}catch (Exception e) {
			System.out.println("GetTrigData Exception:" + e.getMessage());
			return null;
		}
	}
	
	public boolean InitParams(int rootCount) { //check kafka.RootCount(trip number)

		if(this.rootCount != rootCount) { 
			this.rootCount = rootCount;

			KeyTrig.initData();
			msgFilter.values().stream()
				.map(e -> (Map<Integer, List<Evaluable>>)e)
				.flatMap(e -> e.values().stream())
				.filter(o -> o != null)
				.map(o -> (List<Evaluable>)o)
				.flatMap(o -> o.stream())
				.filter(ev -> ev != null)
				.forEach(ev -> {
					ev.trigger.initData();
				});

			return true;
		}
		return true;
	}
	
	public boolean IsAvailable(Tuple dataTuple) {
		try {
			int dataChannel = dataTuple.getInt(RawDataField.DataChannel.getIndex());
			int dataID = dataTuple.getInt(RawDataField.DataID.getIndex()) & 0x00FFFFFF;
			boolean isEvaluable = Optional.ofNullable((Map<Integer,List<Evaluable>>)msgFilter.get(dataChannel)).map(m -> m.containsKey(dataID)).orElse(false);
			if((!"ON".equals(KeyStatus) || (dataID == KeyTrig.id && dataChannel == KeyTrig.ch)) && isEvaluable) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * KeyTrig 를 체크 않하거나, 체크하는 경우 id/ch 가 있는 경우에만 메세지를 배출한다.
	 */
	public boolean IsAvailable(int ch, int id) {
		try {
			int dataChannel = ch;
			int dataID = id & 0x00FFFFFF;
			boolean isEvaluable = Optional.ofNullable((Map<Integer,List<Evaluable>>)msgFilter.get(dataChannel)).map(m -> m.containsKey(dataID)).orElse(false);
			if((!"ON".equals(KeyStatus) || (dataID == KeyTrig.id && dataChannel == KeyTrig.ch)) && isEvaluable) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new PolicyParser("d:/projects/vdms/resources/policy/BM-15C-0003.xml").parse();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
			
			Path policyPath = Paths.get("d:/projects/vdms/resources/policy/BM-16L-0030.xml");
			PolicyParser policyParser =  new PolicyParser(policyPath, documentBuilder);
			System.out.println("result:" + policyParser.msgFilter.size());
			policyParser.InitParams(0);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}