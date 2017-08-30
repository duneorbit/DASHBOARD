package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ScheduledJobsConfig {
	
	private List<ScheduledJobConfig> jobs = new ArrayList<ScheduledJobConfig>();
	
	public ScheduledJobsConfig(){
		
	}

	@XmlElement
	public List<ScheduledJobConfig> getJobs() {
		return jobs;
	}

	public void setJobs(List<ScheduledJobConfig> jobs) {
		this.jobs = jobs;
	}

}
