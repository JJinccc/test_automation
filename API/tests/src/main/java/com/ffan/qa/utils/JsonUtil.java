package com.ffan.qa.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static Object getValue(String jsonText, String expression)
	{
		String[] expPathes = expression.split("\\.");
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LinkedHashMap<String, Object> map = objectMapper.readValue(jsonText, LinkedHashMap.class);
			
			for (int i=0;i<expPathes.length;i++){
				String objKey = expPathes[i];
				int index = -1;
				
				if (objKey.contains("[")){
					Pattern p = Pattern.compile("\\[\\d{1,}\\]");
					Matcher m = p.matcher(objKey);
					if (m.find()){
						index = Integer.parseInt(m.group(0).replaceAll("\\[", "").replaceAll("\\]", ""));
					}
					objKey = objKey.substring(0, objKey.indexOf("["));
				}
				Object object = readMapVal(map, objKey);
				
				if (object == null){
					return null;
				}
				
				if (i < expPathes.length - 1) {
					if (index == -1){
						map = (LinkedHashMap<String, Object>) object;
					} else{
						ArrayList<Object> arr = (ArrayList<Object>)object;
						map = (LinkedHashMap<String, Object>)arr.get(index);
					}
					
				} else {
					if (index==-1){
						return object;
					} else{
						ArrayList<Object> arr = (ArrayList<Object>)object;
						return arr.get(index);
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Fail to serialize json: " + jsonText);
		}
		
		return null;
	}
	
	private static Object readMapVal(Map<String, Object> map, String key)
	{
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			if (entry.getKey().equals(key)){
				return entry.getValue();
			}
		}
		
		return null;
	}
}
