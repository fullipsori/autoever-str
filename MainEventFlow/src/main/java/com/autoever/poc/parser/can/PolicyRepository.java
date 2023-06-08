package com.autoever.poc.parser.can;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.streambase.sb.CompleteDataType;
import com.streambase.sb.Schema;
import com.streambase.sb.Tuple;

public class PolicyRepository {

	public Map<String, PolicyParser> mPolicyMap = null;

	private static PolicyRepository mInstance = new PolicyRepository();
	public static PolicyRepository getInstance() {
		return mInstance;
	}
	
	public static Schema trigDataSchema = new Schema(
			"TRIGDATA",
			new Schema.Field("preTime", CompleteDataType.forDouble()),
			new Schema.Field("postTime", CompleteDataType.forDouble()),
			new Schema.Field("deltaTime", CompleteDataType.forDouble()),
			new Schema.Field("eventName", CompleteDataType.forString()),
			new Schema.Field("category", CompleteDataType.forString()),
			new Schema.Field("status", CompleteDataType.forString()),
			new Schema.Field("value", CompleteDataType.forString())
			);

	public boolean LoadPolicy(String dirPath, String ext) {
		if(dirPath == null || dirPath.isBlank()) return false;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder = factory.newDocumentBuilder();
			mPolicyMap = Files.list(Paths.get(dirPath))
				.filter(path -> path.toString().endsWith(ext))
				.parallel()
				.map(path -> new PolicyParser(path, documentBuilder))
				.collect(Collectors.toConcurrentMap(PolicyParser::GetFileName, Function.identity())) ;
			
			return true;
		} catch (Exception e) {
			System.out.println("LoadPolicy Exp:" + e.getMessage());
			return false;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PolicyRepository.getInstance().LoadPolicy("d:/projects/vdms/resources/policy", "xml");
		PolicyRepository.getInstance().mPolicyMap.entrySet()
			.forEach(entry -> {
				System.out.println("filename:" + entry.getKey() + " size:" + entry.getValue().msgFilter.size());
			});
		System.out.println("Ended");
		
	}
}