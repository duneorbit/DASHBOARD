package com.jspeedbox.tooling.governance.dashboard;

import java.util.Map;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DeveloperSprintSummary {
	
	//private Map<String, String> spSummary = new HashMap<String, String>();
	private ObjectNode summary = null;
	
	public DeveloperSprintSummary(){
		summary = JsonNodeFactory.instance.objectNode();
	}
	
	public void addDataItem(String key, String value){
		summary.put(key, value);
	}
	
	public ObjectNode getSummary(){
		return summary;
	}

}
