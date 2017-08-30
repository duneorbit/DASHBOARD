package com.jspeedbox.tooling.governance.dashboard;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ProjectDashBoard {
	
	private Map<String, Totals> developerTotals = new HashMap<String, Totals>();
	private Map<String, PIDashBoard> piDashboards = new HashMap<String, PIDashBoard>();
	
	public ProjectDashBoard(){
		
	}
	
	public int numOfdashboards(){
		return this.piDashboards.size();
	}
	
	public PIDashBoard getPIDashBoard(String key){
		return this.piDashboards.get(key);
	}
	
	public void addPIDashBoard(String key, PIDashBoard dashboard){
		if(key!=null){
			this.piDashboards.put(key, dashboard);
		}
	}
	
	public int getOverAllCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getTotalComments());
		}
		return overall.intValue();
	}
	
	public int getTeamCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getAllTeamCommentCount());
		}
		return overall.intValue();
	}
	
	public int getTeamJavaCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getJavaTeamCommentCount());
		}
		return overall.intValue();
	}
	
	public int getTeamSQLCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getSQLTeamCommentCount());
		}
		return overall.intValue();
	}
	
	public int getOtherTeamJavaCommentCount(){
		BigDecimal others = new BigDecimal(0);
		BigDecimal all = new BigDecimal(getOverAllJavaCommentCount());
		BigDecimal team = new BigDecimal(getTeamJavaCommentCount());
		others = all.subtract(team);
		return others.intValue();
	}
	
	public int getOtherTeamSQLCommentCount(){
		BigDecimal others = new BigDecimal(0);
		BigDecimal all = new BigDecimal(getOverAllSQLCommentCount());
		BigDecimal team = new BigDecimal(getTeamSQLCommentCount());
		others = all.subtract(team);
		return others.intValue();
	}
	
	public int getOtherTeamCommentCount(){
		BigDecimal others = new BigDecimal(0);
		BigDecimal all = new BigDecimal(getOverAllCommentCount());
		BigDecimal team = new BigDecimal(getTeamCommentCount());
		others = all.subtract(team);
		return others.intValue();
	}
	
	public int getOverAllJavaCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getTotalCommentsJava());
		}
		return overall.intValue();
	}
	
	public int getOverAllSQLCommentCount(){
		BigDecimal overall = new BigDecimal(0);
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			overall = overall.add(piDashBoard.getTotalCommentsSQL());
		}
		return overall.intValue();
	}
	
	public int getOverAllCommits(){
		int commits = 0;
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			commits = commits + piDashBoard.getOverAllCommits();
		}
		return commits;
	}
	
	public int getJavaCommits(){
		int commits = 0;
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			commits = commits + piDashBoard.getJavaCommits();
		}
		return commits;
	}
	
	public int getSQLCommits(){
		int commits = 0;
		Iterator<Entry<String, PIDashBoard>> itr = piDashboards.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, PIDashBoard> entry = itr.next();
			PIDashBoard piDashBoard = entry.getValue();
			commits = commits + piDashBoard.getSqlCommits();
		}
		return commits;
	}
	
	public Map<String, PIDashBoard> getPiDashboards(){
		return piDashboards;
	}
	
	public Map<String, Totals> getDeveloperTotals(){
		developerTotals.clear();
		Iterator<Entry<String, PIDashBoard>> dsahboards = getPiDashboards().entrySet().iterator();
		while(dsahboards.hasNext()){
			Entry<String, PIDashBoard> entry = dsahboards.next();
			Iterator<Entry<String, Reviewer>> reviewers = entry.getValue().getDevelopersStats().entrySet().iterator();
			while(reviewers.hasNext()){
				Entry<String, Reviewer> reviewerEntry = reviewers.next();
				//System.out.println(reviewerEntry.getKey());
				Totals totals = getTotals(reviewerEntry.getKey());
				totals.updateTotals(reviewerEntry.getValue());
			}
		}
		return developerTotals;
	}
	
	public Totals getTotals(String name){
		if(developerTotals.get(name)==null){
			developerTotals.put(name, new Totals(name));
		}
		return developerTotals.get(name);
	}
	
}
