package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProgrammeIncrement extends DateBase implements Comparable<ProgrammeIncrement>{
	
	private String name = null;
	private String startDate = null;
	private String endDate = null;
	
	private List<Sprint> sprints = new ArrayList<Sprint>();
	
	public ProgrammeIncrement(){
		
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

	@XmlElement
	public List<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	@Override
	public int compareTo(ProgrammeIncrement programmeIncrement) {
		return getParsedStartDate().compareTo(programmeIncrement.getParsedStartDate());
	}
	
	public void sort(){
		Collections.sort(sprints, new Comparator<Sprint>(){
			public int compare(Sprint first, Sprint second){
				return first.getParsedStartDate().compareTo(second.getParsedStartDate());
			}
		});
	}

}
