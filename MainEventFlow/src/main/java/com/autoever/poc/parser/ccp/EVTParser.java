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
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.autoever.poc.parser.Parseable;
import com.streambase.sb.util.Pair;


public class EVTParser implements Parseable {
	
	private String filename;
	private Path filePath;
	private Element rootNode;
	private List<Pair<String,String>> measurement_list;
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
		parse();
	}
	

	@Override
	public void parse() {
		// TODO Auto-generated method stub
		
		if(rootNode == null) return;
		try {
			measurement_list = GetElement.apply(rootNode.getChildNodes(), "ChannelSetting")
						.flatMap(el -> GetElement.apply(el.getChildNodes(), "Channel"))
						.flatMap(el -> GetElement.apply(el.getChildNodes(), "Protocols"))
						.flatMap(el -> GetElement.apply(el.getChildNodes(), "CCP"))
						.map(el -> GetElements.apply(el.getChildNodes(), "Measurement"))
						.orElse(Collections.emptyList()).stream()
						.map(el -> new Pair<String,String>(
							GetElement.apply(el.getChildNodes(), "identName").get().getTextContent(),
							GetElement.apply(el.getChildNodes(), "Datatype").get().getTextContent()
							)
						).collect(Collectors.toList());
			genODT(measurement_list);

		} catch(Exception e) { }
	}
	
	
	final Set<String> longStrSet = Set.of("SLONG", "ULONG");
	final Set<String> wordStrSet = Set.of("SWORD", "UWORD");
	final String byteStr = "UBYTE";
	final String byteInit = "BBBBBBBB";

	final BiConsumer<Pair<?,?>, String> alignODT = (el, type) ->  {
		int needCount = ("L".equals(type))? 4 : ("H".equals(type))? 2 : 1;
		odt_map.stream().filter(d -> ((Integer)d[1]) >= needCount).findFirst().ifPresent(a -> {
			((ArrayList<String>)a[0]).add((String)el.getFirst());
			a[1] = (Integer)a[1] - needCount;
			a[2] = ((String)a[2]).concat(type);
		});
	};
	
	
	@SuppressWarnings("unchecked")
	public void genODT(List<Pair<String,String>> dataList) {
		odt_map.clear();
		
		int max_odt = 50;
		IntStream.range(0, max_odt)
			.forEach(i -> odt_map.add(new Object[]{new ArrayList<String>(), 7, new String("")}));
		
		Map<String, List<Pair<?,?>>> groups = dataList.stream()
			.collect(Collectors.groupingBy(pair -> {
				if(longStrSet.contains(pair.getSecond())) return "L";
				else if(wordStrSet.contains(pair.getSecond())) return "H";
				else return "B";
			}));
				
		Optional.ofNullable(groups.get("L")).ifPresent(g -> {
			g.stream().forEach(el -> alignODT.accept(el, "L"));
		});
		Optional.ofNullable(groups.get("H")).ifPresent(g -> {
			g.stream().forEach(el -> alignODT.accept(el, "H"));
		});
		Optional.ofNullable(groups.get("B")).ifPresent(g -> {
			g.stream().forEach(el -> alignODT.accept(el, "B"));
		});

		// finalize
		odt_map.stream()
			.filter(el -> ((Integer)el[1]) >= 1)
			.forEach(el -> {
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
			
			evtParser.odt_map.stream()
				.forEach(el -> System.out.printf("[[%s],%d,%s]\n", String.join(",", ((ArrayList<String>)el[0])), (Integer)el[1], (String)el[2]));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
