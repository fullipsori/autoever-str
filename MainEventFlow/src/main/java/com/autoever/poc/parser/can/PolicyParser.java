package com.autoever.poc.parser.can;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.autoever.poc.parser.Parseable;

public class PolicyParser implements Parseable {

	private String filename;
	private Path xmlFilePath;
	private Element rootNode;

	private Optional<Element> preCondition;
	public String KeyStatus;
	public TriggerParser KeyTrig;

	private List<EventParser> EventList = new ArrayList<>();
	private static EventCallback eventCallback = (a,b) -> null;

	public PolicyParser(Path filePath, DocumentBuilder documentBuilder) {
		this.xmlFilePath = filePath;
		this.filename = this.xmlFilePath.getFileName().toString().substring(0, this.xmlFilePath.getFileName().toString().lastIndexOf('.'));
		try {
			Document document = documentBuilder.parse(xmlFilePath.toFile());
			rootNode = document.getDocumentElement();
		} catch (Exception e) {
			rootNode = null;
		}
		preCondition = null;
		EventList.clear();
	}

//	public static BiFunction<NodeList, String, Optional<Element>> GetElement = (nodelist, name) -> IntStream.range(0, nodelist.getLength())
//			.mapToObj(nodelist::item)
//			.filter(n-> n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(name))
//			.map(n -> (Element)n)
//			.findFirst();
//
//	public static BiFunction<NodeList, String, List<Element>> GetElements = (nodelist, name) -> IntStream.range(0, nodelist.getLength())
//			.mapToObj(nodelist::item)
//			.filter(n-> n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(name))
//			.map(n -> (Element)n)
//			.collect(Collectors.toList());

	public long minPreTime;
	public Object[] msgFilter = {null, null, null, null, null, null};

	@SuppressWarnings("unchecked")
	@Override
	public void parse() {
		try {
			if(rootNode == null) return;

			NodeList children = rootNode.getChildNodes();
			preCondition = GetElement.apply(children, "PreCondition");

			preCondition.ifPresent(e -> {
				KeyStatus = e.getAttribute("Key");
				GetElement.apply(e.getChildNodes(), "Trigger").ifPresent(ele -> {
					KeyTrig = new TriggerParser(ele, eventCallback);
				});
			});
			
			for(Element event : GetElements.apply(children, "Event")) {
				EventParser e = new EventParser(event);
				if(minPreTime < e.preTime) {
					minPreTime = e.preTime;
				}
				EventList.add(e);
				
				// [returnVal=[,,],returnVal]
				for(Object el : e.msgTable) {
					List<Object> ta = (List<Object>)el;
					int ch = (Integer)ta.get(0);
					int id = (Integer)ta.get(1);

					if(msgFilter[ch] == null) {
						msgFilter[ch] = new HashMap<Integer,Object>();
					}
//					((Map<Integer,Object>)msgFilter[ch]).compute(id, (k, v) -> (v == null)? new ArrayList<>(Arrays.asList(ta.get(2))): ((ArrayList<Object>)v).add(ta.get(2)));
					if(((Map<Integer,Object>)msgFilter[ch]).containsKey(id)) {
						((ArrayList<Object>)((Map<Integer,Object>)msgFilter[ch]).get(id)).add(ta.get(2));
					}else {
						((Map<Integer,Object>)msgFilter[ch]).put(id, new ArrayList<>(Arrays.asList(ta.get(2))));
					}
					if(ta.size() > 3) { //tpdtID  type?
						int tpdtID = (Integer)ta.get(3);
						if(((Map<Integer,Object>)msgFilter[ch]).containsKey(id)) {
							((ArrayList<Object>)((Map<Integer,Object>)msgFilter[ch]).get(tpdtID)).add(ta.get(2));
						}else {
							((Map<Integer,Object>)msgFilter[ch]).put(tpdtID, new ArrayList<>(Arrays.asList(ta.get(2))));
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		};
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// new PolicyParser("d:/projects/vdms/resources/policy/BM-15C-0008.xml").parse();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
			
			Path policyPath = Paths.get("e:/projects/str/workspace/MainEventFlow/src/main/resources/can_policy.xml");
			PolicyParser policyParser =  new PolicyParser(policyPath, documentBuilder);
			policyParser.parse();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}