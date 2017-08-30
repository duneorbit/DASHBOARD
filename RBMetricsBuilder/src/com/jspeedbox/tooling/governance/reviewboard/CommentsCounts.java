package com.jspeedbox.tooling.governance.reviewboard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class CommentsCounts {
	
	private List<Comments> comments = null;
	
	public CommentsCounts(){
		
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
	
	

}
