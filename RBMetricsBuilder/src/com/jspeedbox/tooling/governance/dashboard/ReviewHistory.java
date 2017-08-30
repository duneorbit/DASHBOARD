package com.jspeedbox.tooling.governance.dashboard;

import java.util.HashMap;
import java.util.Map;

public class ReviewHistory {
	
	private String summary = null;
	private String lastActivityDate = null;
	
	private Map<String, CommitReview> commitReviews = new HashMap<String, CommitReview>();
	
	public ReviewHistory(){
		
	}
	
	public CommitReview getCommitReview(String key){
		if(commitReviews.get(key)==null){
			commitReviews.put(key, new CommitReview());
		}
		return commitReviews.get(key);
	}
	
	public Map<String, CommitReview> getCommitReviews(){
		return commitReviews;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(String lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}
	
	

}
