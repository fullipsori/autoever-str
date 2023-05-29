package com.autoever.poc.parser.ccp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.autoever.poc.parser.Parseable;


public class EVTParser implements Parseable {
	
	private String filename;
	private Path filePath;
	private Element rootNode;
	private List<Element> measurements;
	
	private List<List<String>> measurement_list;
	public List<Object[]> odt_map = new ArrayList<>();
	
	public EVTParser(Path filePath, DocumentBuilder documentBuilder) {
		this.filePath = filePath;
		this.filename = this.filePath.getFileName().toString().substring(0, this.filePath.getFileName().toString().lastIndexOf('.'));
		try {
			Document document = documentBuilder.parse(filePath.toFile());
			rootNode = document.getDocumentElement();
		} catch (Exception e) {
			rootNode = null;
		}
	}
	

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		
		if(rootNode == null) return;
		try {
			measurements = Parseable.GetElement.apply(rootNode.getChildNodes(), "ChannelSetting")
						.filter(el -> el != null)
						.map(el -> Parseable.GetElement.apply(el.getChildNodes(), "Channel").orElse(null))
						.filter(el -> el != null)
						.map(el -> Parseable.GetElement.apply(el.getChildNodes(), "Protocols").orElse(null))
						.filter(el -> el != null)
						.map(el -> Parseable.GetElement.apply(el.getChildNodes(), "CCP").orElse(null))
						.filter(el -> el != null)
						.map(el -> Parseable.GetElements.apply(el.getChildNodes(), "Measurement"))
						.orElse(Collections.emptyList());
			
			measurement_list = measurements.stream()
					.map(el -> Arrays.asList(
							Parseable.GetElement.apply(el.getChildNodes(), "identName").get().getTextContent(),
							Parseable.GetElement.apply(el.getChildNodes(), "Datatype").get().getTextContent()
							))
					.collect(Collectors.toList());

//					collect(Collectors.toMap(el -> el.getAttribute("identName"), el -> el.getAttribute("Datatype")));

			genODT(measurement_list);

		} catch(Exception e) { }
	}
	
	
	final Set<String> longStrSet = Set.of("SLONG", "ULONG");
	final Set<String> wordStrSet = Set.of("SWORD", "UWORD");
	final String byteStr = "UBYTE";
	final String byteInit = "BBBBBBBB";

	@SuppressWarnings("unchecked")
	public void genODT(List<List<String>> dataList) {
		odt_map.clear();
		
		int max_odt = 50;
		IntStream.range(0, max_odt)
			.forEach(i -> odt_map.add(new Object[]{new ArrayList<String>(), 7, new String("")}));
		
		for(List<String> m : measurement_list) {
			if(longStrSet.contains(m.get(1))) {
				odt_map.stream()
					.filter(l -> ((Integer)l[1]) >= 4)
					.limit(1)
					.forEach(el -> {
						((ArrayList<String>)el[0]).add(m.get(0));
						el[1] = (Integer)el[1] - 4;
						el[2] = ((String)el[2]).concat("L");
					});
			}
		}

		for(List<String> m : measurement_list) {
			if(wordStrSet.contains(m.get(1))) {
				odt_map.stream()
					.filter(l -> ((Integer)l[1]) >= 2)
					.limit(1)
					.forEach(el -> {
						((ArrayList<String>)el[0]).add(m.get(0));
						el[1] = (Integer)el[1] - 2;
						el[2] = ((String)el[2]).concat("H");
					});
			}
		}

		for(List<String> m : measurement_list) {
			if(byteStr.equals(m.get(1))) {
				odt_map.stream()
					.filter(l -> ((Integer)l[1]) >= 1)
					.limit(1)
					.forEach(el -> {
						((ArrayList<String>)el[0]).add(m.get(0));
						el[1] = (Integer)el[1] - 1;
						el[2] = ((String)el[2]).concat("B");
					});
			}
		}
		
		// finalize
		odt_map.stream()
			.filter(el -> ((Integer)el[1]) >= 1)
			.forEach(el -> {
//				((String)el[2]).concat(IntStream.range(0, ((Byte)el[1])).mapToObj(i -> new String("B")).toArray() );
				el[2] = ((String)el[2]).concat(byteInit.substring(0, ((Integer)el[1])));
			});
	}



	public static void main(String[] args) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
			
			Path evtPath = Paths.get("e:/projects/str/workspace/MainEventFlow/src/main/resources/ccp_odt.evt");
			EVTParser evtParser =  new EVTParser(evtPath, documentBuilder);
			evtParser.parse();
			
			evtParser.odt_map.stream()
				.forEach(el -> System.out.printf("[[%s],%d,%s]\n", String.join(",", ((ArrayList<String>)el[0])), (Integer)el[1], (String)el[2]));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
