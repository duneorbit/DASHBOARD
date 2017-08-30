package com.jspeedbox.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONUtils {
	
	public static ObjectNode jsonStringToObjectNode(String json){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = null;
		try{
			node = mapper.readValue(json, ObjectNode.class);
		}catch(Exception e){
			e.printStackTrace();
		}
		return node;
	}

}
