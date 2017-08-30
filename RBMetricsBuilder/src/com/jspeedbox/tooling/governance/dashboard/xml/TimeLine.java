package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeLine {
	
	private List<TimeLineEntry> timeLineEntries = new ArrayList<TimeLineEntry>();
	
	public TimeLine(){
		
	}

	@XmlElement
	public List<TimeLineEntry> getTimeLineEntries() {
		return timeLineEntries;
	}

	public void setTimeLineEntries(List<TimeLineEntry> timeLineEntries) {
		this.timeLineEntries = timeLineEntries;
	}
	
	

}
