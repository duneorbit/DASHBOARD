package com.jspeedbox.tooling.governance.reviewboard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class RBFiles {
	
	private String num_diffs = null;
	private String lastActivityTimestamp = null;
	private String summary = null;
	
	private List<RBFile> files = null;
	
	public RBFiles(){
		
	}

	public String getNum_diffs() {
		return num_diffs;
	}

	public void setNum_diffs(String num_diffs) {
		this.num_diffs = num_diffs;
	}

	public List<RBFile> getFiles() {
		return files;
	}

	public void setFiles(List<RBFile> files) {
		this.files = files;
	}

	public String getLastActivityTimestamp() {
		return lastActivityTimestamp;
	}

	public void setLastActivityTimestamp(String lastActivityTimestamp) {
		this.lastActivityTimestamp = lastActivityTimestamp;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	

}
