package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

public class DeveloperHistory {
	
	private String developer = null;
	
	private Map<String, ReviewHistory> reviewHistory = new HashMap<String, ReviewHistory>();
	
	public DeveloperHistory(){
		
	}
	
	public ReviewHistory getReviewHistory(String key){
		if(reviewHistory.get(key)==null){
			this.developer = key;
			reviewHistory.put(key, new ReviewHistory());
		}
		return reviewHistory.get(key);
	}
	
	public Map<String, ReviewHistory> getReviewHistory(){
		return reviewHistory;
	}
	
	public String getDeveloper(){
		return this.developer;
	}

}
