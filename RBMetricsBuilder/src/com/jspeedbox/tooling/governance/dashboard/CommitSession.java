package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

public class CommitSession {
	
	private Map<String, CommitSessionItem> commitSessionItemTree = new HashMap<String, CommitSessionItem>();
	
	public CommitSession(){
		
	}
	
	public Map<String, CommitSessionItem> getCommitSessionItemTree(){
		return commitSessionItemTree;
	}

}
