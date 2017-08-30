package com.jspeedbox.tooling.governance.reviewboard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class RBFile {
	
	private List<CommentsCounts> comment_counts = null;
	private String revision = null;
	private String dest_revision = null;

	private String dest_filename = null;
	
	private boolean newfile = false;
	
	public RBFile(){
		
	}

	public List<CommentsCounts> getComment_counts() {
		return comment_counts;
	}

	public void setComment_counts(List<CommentsCounts> comment_counts) {
		this.comment_counts = comment_counts;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getDest_filename() {
		return dest_filename;
	}

	public void setDest_filename(String dest_filename) {
		this.dest_filename = dest_filename;
	}

	public boolean isNewfile() {
		return newfile;
	}

	public void setNewfile(boolean newfile) {
		this.newfile = newfile;
	}
	
	public String getDest_revision() {
		return dest_revision;
	}

	public void setDest_revision(String dest_revision) {
		this.dest_revision = dest_revision;
	}
	
}
