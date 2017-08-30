package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableReviewSummary {

	private String developer = null;
	private String sprint = null;

	private List<RowSummary> rowSummary = new ArrayList<RowSummary>();
	
	public TableReviewSummary(){
		
	}

	@XmlElement
	public List<RowSummary> getRowSummary() {
		return rowSummary;
	}

	public void setRowSummary(List<RowSummary> rowSummary) {
		this.rowSummary = rowSummary;
	}
	
	@XmlAttribute
	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	@XmlAttribute
	public String getSprint() {
		return sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}
	
}
