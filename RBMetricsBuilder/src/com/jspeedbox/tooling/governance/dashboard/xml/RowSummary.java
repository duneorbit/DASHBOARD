package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.jspeedbox.utils.DateUtils;

@XmlRootElement
public class RowSummary implements Comparable<RowSummary>{
	
	private String summary = null;
	private String date = null;
	
	private String revisions = null;
	
	private Date parsedDate = null;
	
	public RowSummary(){
		
	}

	@XmlAttribute
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@XmlAttribute
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		this.parsedDate = DateUtils.formatForwardSlashDate(date);
	}

	@XmlAttribute
	public String getRevisions() {
		return revisions;
	}

	public void setRevisions(String revisions) {
		this.revisions = revisions;
	}
	
	public Date getParsedDate(){
		return parsedDate ;
	}

	@Override
	public int compareTo(RowSummary in) {
		return getParsedDate().compareTo(in.getParsedDate());
	}

	
}
