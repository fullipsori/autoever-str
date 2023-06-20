package com.autoever.poc.parser.ccp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.autoever.poc.parser.Parseable;
import com.streambase.sb.Tuple;
import com.streambase.sb.util.Pair;


public class ODTParser implements Parseable {
	
	private String filename;
	private Path filePath;
	private Element rootNode;
	
	private int rootCount = 0;
	public List<Pair<Double, Tuple>> prevTuples= new ArrayList<>();
	private final double maxInterval = 5.0;
	
	private List<Pair<String,String>> measurement_list;
	public List<Object[]> odt_map = new ArrayList<>();
	public int ccpRawEndCmd = 0x36;
	
	public ODTParser(Path filePath, DocumentBuilder documentBuilder) {
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
	
	public String GetFilename() {
		return filename;
	}
	
	public boolean InitParams(int rootCount) {

		if(this.rootCount != rootCount) { 
			this.rootCount = rootCount;
			prevTuples.clear();
			return true;
		}
		return true;
	}
	
	public Tuple getMatchedTupleByInterval(Tuple dataTuple, double realTime, double minInterval) {
		// removed over maxInterval
		final double removeTime = realTime - maxInterval;
		final double searchTime = realTime - minInterval;
		prevTuples.removeIf(p -> p.first <= removeTime);
		//search matched tuple
		Tuple matched = prevTuples.stream().filter(p -> p.first <= searchTime).findFirst().map(Pair::getSecond).orElse(null);
		//add current tuple
		prevTuples.add(0, new Pair<Double, Tuple>(realTime, dataTuple));
		return matched;
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

	@SuppressWarnings("unchecked")
	final BiConsumer<Pair<?,?>, String> alignODT = (el, type) ->  {
		int needCount = ("L".equals(type))? 4 : ("H".equals(type))? 2 : 1;
		odt_map.stream().filter(d -> ((Integer)d[1]) >= needCount).findFirst().ifPresent(a -> {
			((ArrayList<String>)a[0]).add((String)el.getFirst());
			a[1] = (Integer)a[1] - needCount;
			a[2] = ((String)a[2]).concat(type);
		});
	};
	
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

		// last Element Index (start: 0x0a + (first_7 -1) -1)
		ccpRawEndCmd = CCPPreProcessor.ccpStartCmd - 1 +  IntStream.range(0, odt_map.size()).filter(d -> ((Integer)((Object[])odt_map.get(d))[1]) == 7).findFirst().orElse(odt_map.size());
		if(ccpRawEndCmd > CCPPreProcessor.ccpEndCmd) ccpRawEndCmd = CCPPreProcessor.ccpEndCmd;
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = factory.newDocumentBuilder();
			
			Path evtPath = Paths.get("d:/projects/autoever-str/MainEventFlow/src/main/resources/evt/219054.evt");
			ODTParser evtParser =  new ODTParser(evtPath, documentBuilder);
			
			evtParser.odt_map.stream()
				.forEach(el -> System.out.printf("[[%s],%d,%s]\n", String.join(",", ((ArrayList<String>)el[0])), (Integer)el[1], (String)el[2]));
			System.out.printf("CCP End:0x%x\n" , evtParser.ccpRawEndCmd);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
