package com.autoever.poc.parser.can;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PolicyRepository {

	public Map<String, PolicyParser> mPolicyMap = new HashMap<>();

	private static PolicyRepository mInstance = new PolicyRepository();
	public static PolicyRepository getInstance() {
		return mInstance;
	}

	public static void LoadPolicy(String dirPath, String ext) {
		if(dirPath == null || dirPath.isBlank()) return;
		
		Map<String, PolicyParser> policyMap = PolicyRepository.getInstance().mPolicyMap;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = factory.newDocumentBuilder();
			policyMap.clear();
			Files.list(Paths.get(dirPath))
				.filter(path -> path.toString().endsWith(ext))
				.forEach(path -> {
					try {
						String filename = path.getFileName().toString();
						PolicyParser policy = new PolicyParser(path, documentBuilder);
						policy.parse();
						policyMap.put(
								filename.substring(0, filename.lastIndexOf('.')), 
								policy
							);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
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