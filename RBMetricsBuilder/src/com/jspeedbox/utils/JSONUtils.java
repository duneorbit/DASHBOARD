package com.jspeedbox.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONUtils {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(JSONUtils.class);
	
	public static ObjectNode jsonStringToObjectNode(String json){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = null;
		try{
			node = mapper.readValue(json, ObjectNode.class);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "jsonStringToObjectNode", e);
		}
		return node;
	}

}
