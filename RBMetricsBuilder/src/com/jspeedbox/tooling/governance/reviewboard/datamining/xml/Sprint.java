package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Sprint extends DateBase implements Comparable<Sprint>{
	
	private String name = null;
	private String startDate = null;
	private String endDate = null;
	
	public Sprint(){
		
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate.replaceAll("/", "-");
	}

	@XmlAttribute
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate.replaceAll("/", "-");
	}

	@Override
	public int compareTo(Sprint sprint) {
		return getParsedStartDate().compareTo(sprint.getParsedStartDate());
	}
	
	

}
