package com.jspeedbox.tooling.governance.dashboard;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class PIDashBoard extends PIDashBoardBase{
	
	private int javaCommits = 0;
	private int sqlCommits = 0;
	private int overAllCommits = 0;
	private int superficial = 0;
	private int critical = 0;
	private int newFileComment = 0;
	private int existingFileComment = 0;
	
	private String piName = null;
	private String startDate = null;
	private String endDate = null;
	
	private BigDecimal totalComments = new BigDecimal(0);
	private BigDecimal totalCommentsJava = new BigDecimal(0);
	private BigDecimal totalCommentsSQL = new BigDecimal(0);
	private BigDecimal teamComments = new BigDecimal(0);
	private BigDecimal javaTeamComments = new BigDecimal(0);
	private BigDecimal sqlTeamComments = new BigDecimal(0);
	
	private DateTimeFormatter piSourceFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
	private DateTimeFormatter piSourceFormatStartDate = DateTimeFormat.forPattern("dd/MM/yyyy");
	private Date parsedDate = null;
	
	private Map<String, Reviewer> developersStats = new HashMap<String, Reviewer>();
	private Map<String, DeveloperSummaryDashBoard> developerSummaryDashBoards = new HashMap<String, DeveloperSummaryDashBoard>();
	
	public PIDashBoard(String name, String start, String end){
		this.piName = name;
		this.parsedDate = piSourceFormatStartDate.parseDateTime(start).toDate();
		this.startDate = start;
		this.endDate = end;
		initSprintSummaryDashBoards(this.piName);
	}
	
	public void incNewFileComment(){
		newFileComment = newFileComment + 1;
	}
	
	public void incExistingFileComment(){
		existingFileComment = existingFileComment + 1;
	}
	
	public int getNewFileCommentCount(){
		return newFileComment;
	}
	
	public int getExistingFileCommentCount(){
		return existingFileComment;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incSuperficial(){
		this.superficial = this.superficial + 1;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incCritical(){
		this.critical = this.critical + 1;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public Reviewer getDevelopersStats(String key){
		return this.developersStats.get(key);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public DeveloperSummaryDashBoard getDeveloperSummaryDashBoard(String key){
		if(developerSummaryDashBoards.get(key)==null){
			developerSummaryDashBoards.put(key, new DeveloperSummaryDashBoard(key));
		}
		return this.developerSummaryDashBoards.get(key);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addDeveloperSummaryDashBoard(String key, DeveloperSummaryDashBoard dashboard){
		this.developerSummaryDashBoards.put(key, dashboard);
	}
	
	public Map<String, DeveloperSummaryDashBoard> getDeveloperDashBoards(){
		return developerSummaryDashBoards;
	}

	public String getPiName() {
		return piName;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addTotalComments(BigDecimal amount){
		totalComments = totalComments.add(amount);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addTotalCommentsJava(BigDecimal amount){
		totalCommentsJava = totalCommentsJava.add(amount);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addTotalCommentsSQL(BigDecimal amount){
		totalCommentsSQL = totalCommentsSQL.add(amount);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addTotalTeamCommentsJava(BigDecimal amount){
		javaTeamComments = javaTeamComments.add(amount);
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addTotalTeamCommentsSQL(BigDecimal amount){
		sqlTeamComments = sqlTeamComments.add(amount);
	}
	
	public BigDecimal getTotalComments(){
		return totalComments;
	}
	
	public BigDecimal getTotalCommentsJava(){
		return totalCommentsJava;
	}
	
	public BigDecimal getTotalCommentsSQL(){
		return totalCommentsSQL;
	}

	public int getJavaCommits() {
		return javaCommits;
	}

	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incJavaCommits() {
		this.javaCommits = this.javaCommits + 1;
	}

	public int getSqlCommits() {
		return sqlCommits;
	}

	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incSqlCommits() {
		this.sqlCommits = this.sqlCommits + 1;
	}

	public int getOverAllCommits() {
		return overAllCommits;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addAllTeamComments(BigDecimal teamComment){
		this.teamComments = this.teamComments.add(teamComment);
	}

	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void incOverAllCommits() {
		this.overAllCommits = this.overAllCommits + 1;
	}
	
	public BigDecimal getAllTeamCommentCount(){
		return this.teamComments;
	}
	
	public BigDecimal getJavaTeamCommentCount(){
		return this.javaTeamComments;
	}
	
	public BigDecimal getSQLTeamCommentCount(){
		return this.sqlTeamComments;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public Reviewer getReviewer(String name){
		if(developersStats.get(name)==null){
			developersStats.put(name, new Reviewer(name));
		}
		return developersStats.get(name);
	}
	
	public Map<String, Reviewer> getDevelopersStats(){
		return developersStats;
	}
	
	public void updateCommentsCountForDeveloper(String developer, String date){
		SprintSummaryDashBoard dashboard = getSprintDashboard(date);
		if(dashboard!=null){
			dashboard.updateCommentCount(developer);
		}
	}
	
	public SprintSummaryDashBoard getSprintDashboard(String date){
		Date parsedDate = piSourceFormat.parseDateTime(date).toDate();
		try{
			Iterator<Entry<String, SprintSummaryDashBoard>> itr = getPiSprintDashboard().entrySet().iterator();
			while(itr.hasNext()){
				Entry<String, SprintSummaryDashBoard> entry = itr.next();
				if(entry.getValue().isSprint(parsedDate)){
					return entry.getValue();
				}
			}
		}catch(Exception e){
			//TODO - SEND NOTIFICATION messages to web client that sprint dates need to be created
			//also add logging
		}
		return null;
	}
	
	public Map<String, SprintSummaryDashBoard> getPiSprintDashboards(){
		return getPiSprintDashboard();
	}
	
	public Date getParsedDate(){
		return parsedDate;
	}

	
	
	//Map<String, PIDashBoard> piSortedDashBoards
	public static Map<String, PIDashBoard> sortByPIStartDate(Map<String, PIDashBoard> unsorted){
		List<Entry<String, PIDashBoard>> list = new LinkedList(unsorted.entrySet());
		
			Collections.sort(list, new Comparator<Entry<String, PIDashBoard>>(){
				public int compare(Entry<String, PIDashBoard> first, Entry<String, PIDashBoard> second){
					int s = first.getValue().getParsedDate().compareTo(second.getValue().getParsedDate());
					return s;
				}
			});
		
		
		Map<String, PIDashBoard> sorted = new LinkedHashMap();
		for(Entry<String, PIDashBoard> entry : list){
			sorted.put(entry.getKey(), entry.getValue());
		}
		
		return sorted;
	}

}
