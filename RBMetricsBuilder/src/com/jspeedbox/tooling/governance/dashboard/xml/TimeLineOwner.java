package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeLineOwner {
	
	private String owner = null;
	
	private List<ReviewTimeLine> reviewTimeLines = new ArrayList<ReviewTimeLine>();
	
	public TimeLineOwner(){
		
	}

	@XmlAttribute
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@XmlElement
	public List<ReviewTimeLine> getReviewTimeLines() {
		return reviewTimeLines;
	}

	public void setReviewTimeLines(List<ReviewTimeLine> reviewTimeLines) {
		this.reviewTimeLines = reviewTimeLines;
	}
	
	

}
