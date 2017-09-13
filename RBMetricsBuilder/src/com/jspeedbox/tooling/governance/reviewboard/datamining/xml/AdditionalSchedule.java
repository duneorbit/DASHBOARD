package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AdditionalSchedule {
	
	private long lastRunTime = 0L;
	
	private boolean success = false;
	
	private String msg = null;
	private String pattern = null;
	private String patternKey = null;
	private String dashboard = null;
	private String scheduleName = null;
	private String lastRunBy = null;

	public AdditionalSchedule(){
		
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@XmlElement
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@XmlElement
	public String getDashboard() {
		return dashboard;
	}

	public void setDashboard(String dashboard) {
		this.dashboard = dashboard;
		setScheduleName("ADDITIONAL-"+dashboard);
	}

	@XmlElement
	public String getScheduleName() {
		return scheduleName;
	}

	private void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	
	@XmlElement
	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	@XmlElement
	public String getLastRunBy() {
		return lastRunBy;
	}

	public void setLastRunBy(String lastRunBy) {
		this.lastRunBy = lastRunBy;
	}

	public String getPatternKey() {
		return patternKey;
	}

	public void setPatternKey(String patternKey) {
		this.patternKey = patternKey;
	}
	
	public AdditionalSchedule copy() throws CloneNotSupportedException{
		AdditionalSchedule additionalSchedule = new AdditionalSchedule();
		additionalSchedule.setDashboard(this.getDashboard());
		additionalSchedule.setLastRunBy(this.getLastRunBy());
		additionalSchedule.setLastRunTime(this.getLastRunTime());
		additionalSchedule.setMsg(this.getMsg());
		additionalSchedule.setPattern(this.getPattern());
		additionalSchedule.setPatternKey(this.getPatternKey());
		additionalSchedule.setScheduleName(this.getScheduleName());
		additionalSchedule.setSuccess(this.isSuccess());
		return additionalSchedule;
	}

}
