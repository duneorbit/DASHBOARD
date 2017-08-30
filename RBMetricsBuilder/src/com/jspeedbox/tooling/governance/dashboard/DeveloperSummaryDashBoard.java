package com.jspeedbox.tooling.governance.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class DeveloperSummaryDashBoard {
	
	private String developerName = null;
	
	private int developerTotalComments = 0;
	private int developerCommentsJava = 0;
	private int developerCommentsSQL = 0;
	
	
	public DeveloperSummaryDashBoard(String developerName){
		this.developerName = developerName;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incDeveloperTotalComments(){
		this.developerTotalComments = this.developerTotalComments + 1;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incDeveloperCommentsJava(){
		this.developerCommentsJava = developerCommentsJava + 1;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incDeveloperCommentsSQL(){
		this.developerCommentsSQL = this.developerCommentsSQL + 1;
	}

	public int getDeveloperTotalComments() {
		return developerTotalComments;
	}

	public int getDeveloperCommentsJava() {
		return developerCommentsJava;
	}

	public int getDeveloperCommentsSQL() {
		return developerCommentsSQL;
	}

	public String getDeveloperName() {
		return developerName;
	}
	
	

}
