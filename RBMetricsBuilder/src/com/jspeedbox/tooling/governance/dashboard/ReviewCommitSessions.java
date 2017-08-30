package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

public class ReviewCommitSessions {
	
	private Map<String, CommitSession> committedSessions = new HashMap<String, CommitSession>();
	
	public ReviewCommitSessions(){
		
	}
	
	public Map<String, CommitSession> getCommittedSessions(){
		return committedSessions;
	}

}
