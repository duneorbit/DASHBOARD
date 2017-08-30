package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeLineEntry {
	
	private int revision = 0;
	
	private List<FileEntry> files = new ArrayList<FileEntry>();
	
	public TimeLineEntry(){
		
	}
	
	public TimeLineEntry(int revision){
		this.revision = revision;
	}
	
	@XmlAttribute
	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	@XmlElement
	public List<FileEntry> getFiles() {
		return files;
	}

	public void setFiles(List<FileEntry> files) {
		this.files = files;
	}
	
	

}
