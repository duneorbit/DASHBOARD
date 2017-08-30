package com.jspeedbox.tooling.governance.dashboard;

import java.util.List;

import com.jspeedbox.tooling.governance.reviewboard.RBFile;

public class CommitReview{
	
	private int teamCoummentCount = 0;
	private int reviewersCommentCount = 0;
	
	private String lastActivityTimestamp = null;
	
	private List<RBFile> committedFiles = null;
	
	public CommitReview(){
		
	}

	public String getLastActivityTimestamp() {
		return lastActivityTimestamp;
	}

	public void setLastActivityTimestamp(String lastActivityTimestamp) {
		this.lastActivityTimestamp = lastActivityTimestamp;
	}

	public List<RBFile> getCommittedFiles() {
		return committedFiles;
	}

	public void setCommittedFiles(List<RBFile> committedFiles) {
		this.committedFiles = committedFiles;
	}
	
	public void incTeamCoummentCount(){
		this.teamCoummentCount = teamCoummentCount + 1;
	}
	
	public void incReviewersCommentCount(){
		this.reviewersCommentCount = reviewersCommentCount + 1;
	}

}
