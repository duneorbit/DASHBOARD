package com.jspeedbox.tooling.governance.dashboard;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DeveloperSprintSummaryWrapper {

	private ArrayNode root = null;
	
	public DeveloperSprintSummaryWrapper(){
		root = JsonNodeFactory.instance.arrayNode();
	}
	
	public void addData(ObjectNode node){
		root.add(node);
	}
	
	public ArrayNode getData(){
		return root;
	}

}
