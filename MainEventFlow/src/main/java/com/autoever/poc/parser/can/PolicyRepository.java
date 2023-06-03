package com.autoever.poc.parser.can;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PolicyRepository {

	public Map<String, PolicyParser> mPolicyMap = null;

	private static PolicyRepository mInstance = new PolicyRepository();
	public static PolicyRepository getInstance() {
		return mInstance;
	}
	
	public void setPolicy(Map<String,PolicyParser> map) {
		mPolicyMap  = map;
	}

	//fullipsori
	public static void LoadPolicy(String dirPath, String ext) {
		if(dirPath == null || dirPath.isBlank()) return;
		
//		Map<String, PolicyParser> policyMap = PolicyRepository.getInstance().mPolicyMap;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = factory.newDocumentBuilder();
			PolicyRepository.getInstance().setPolicy( 
				Files.list(Paths.get(dirPath))
				.filter(path -> path.toString().endsWith(ext))
				.parallel()
				.map(path -> new PolicyParser(path, documentBuilder))
				.collect(Collectors.toConcurrentMap(PolicyParser::GetFileName, Function.identity())) 
			);

		} catch (Exception e) {
			return;
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadPolicy("d:/projects/vdms/resources/policy", "xml");
		
	//	PolicyRepository.getInstance().mPolicyMap.entrySet()
	//		.forEach(entry -> {
	//			System.out.println("filename:" + entry.getKey() + " precondition:" + entry.getValue().getDocumentElement().getAttribute("timeZone"));
	//		});
		
	}
}