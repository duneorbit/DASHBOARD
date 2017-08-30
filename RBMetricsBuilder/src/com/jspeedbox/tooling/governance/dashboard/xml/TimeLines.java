package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeLines {
	
	private String spintName = null;
	
	private List<TimeLineOwner> timeLineOwners = new ArrayList<TimeLineOwner>();
	
	public TimeLines(){
		
	}
	
	public TimeLines(String spintName){
		this.spintName = spintName;
	}

	@XmlAttribute
	public String getSpintName() {
		return spintName;
	}

	public void setSpintName(String spintName) {
		this.spintName = spintName;
	}

	@XmlElement
	public List<TimeLineOwner> getTimeLineOwners() {
		return timeLineOwners;
	}

	public void setTimeLineOwners(List<TimeLineOwner> timeLineOwners) {
		this.timeLineOwners = timeLineOwners;
	}
	
	

}
