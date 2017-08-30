package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileEntry {
	
	private String fileName = null;
	
	private List<CommentEntry> comments = new ArrayList<CommentEntry>();
	
	public FileEntry(){
		
	}
	
	public FileEntry(String fileName){
		this.fileName = fileName;
	}
	
	@XmlAttribute
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlElement
	public List<CommentEntry> getComments() {
		return comments;
	}

	public void setComments(List<CommentEntry> comments) {
		this.comments = comments;
	}
	
	

}
