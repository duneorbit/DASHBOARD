package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jspeedbox.utils.DateUtils;
import com.jspeedbox.utils.IOUtils;

@XmlRootElement
public class ScheduledJobConfig {
	
	private long lastRunTime = 0L;
	
	private String name = null;
	private String group = null;
	private String lastRunBy = null;
	private String scheduleName = null;
	private String lastRunTimeFormatted = null;

	private AdditionalSchedule additionalSchedule = null;
	
	public ScheduledJobConfig(){
		
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setScheduleName(IOUtils.PREFIX_NIGHTLY+name);
	}

	@XmlElement
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	@XmlElement
	public AdditionalSchedule getAdditionalSchedule() {
		return additionalSchedule;
	}

	public void setAdditionalSchedule(AdditionalSchedule additionalSchedule) {
		this.additionalSchedule = additionalSchedule;
	}

	@XmlElement
	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
		setLastRunTimeFormatted(DateUtils.formatSimpleFormatterDateTime(this.lastRunTime));
	}

	@XmlElement
	public String getLastRunBy() {
		return lastRunBy;
	}

	public void setLastRunBy(String lastRunBy) {
		this.lastRunBy = lastRunBy;
	}

	@XmlElement
	public String getScheduleName() {
		return scheduleName;
	}

	private void setLastRunTimeFormatted(String lastRunTimeFomatted) {
		this.lastRunTimeFormatted = lastRunTimeFomatted;
	}

	private void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@XmlElement
	public String getLastRunTimeFormatted() {
		return lastRunTimeFormatted;
	}
	
	

}
