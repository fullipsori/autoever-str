package com.autoever.poc.parser.ccp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class EVTRepository {

	public Map<String, EVTParser> mODTMap = null;

	private static EVTRepository mInstance = new EVTRepository();
	public static EVTRepository getInstance() {
		return mInstance;
	}
	
	public void setODT(Map<String,EVTParser> map) {
		mODTMap  = map;
	}

	public static void LoadEVT(String dirPath, String ext) {
		if(dirPath == null || dirPath.isBlank()) return;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = factory.newDocumentBuilder();
			EVTRepository.getInstance().setODT( 
				Files.list(Paths.get(dirPath))
				.filter(path -> path.toString().endsWith(ext))
				.parallel()
				.map(path -> new EVTParser(path, documentBuilder))
				.collect(Collectors.toConcurrentMap(EVTParser::GetFilename, Function.identity())) 
			);

		} catch (Exception e) {
			return;
		}
	}
}
