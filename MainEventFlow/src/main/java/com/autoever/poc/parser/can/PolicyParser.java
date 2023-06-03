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
import java.util.stream.Stream;

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

	public String KeyStatus = "ON";
	public TriggerParser KeyTrig;

//	private List<EventParser> EventList = new ArrayList<>();
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
		//fullipsori
//		EventList.clear();
		parse();
	}
	
	public String GetFileName() {
		return filename;
	}

	public long minPreTime = 0;
	public Map<Integer, ?> msgFilter;


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
			
			List<Element> eventList = GetElements.apply(children, "Event");
			msgFilter = eventList.stream()
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
								Collectors.mapping(ta -> (BiFunction<?,?,?>)ta.get(2), Collectors.toList())  )));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
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
			System.out.println("result:" + policyParser.msgFilter.size());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}