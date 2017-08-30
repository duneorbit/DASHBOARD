package com.jspeedbox.tooling.governance.dashboard;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Reviewer {
	
	private String name = null;
	
	private boolean isMemberOfReviewTeam = false;
	
	private List<String> allComments = new ArrayList<String>();
	
	private int duplicateComments = 0;
	private int peformanceComments = 0;
	private int cosmeticComments = 0;
	private int miscComments = 0;
	
	private static ReviewBoardHelper helper = null;
	
	public Reviewer(String name){
		this.name = name;
		helper = ReviewBoardHelper.getInstance();
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addToAllComments(String comment){
		isDuplicate(comment);
		allComments.add(comment);
		if(containsPerfomanceVerbiage(comment)){
			peformanceComments = peformanceComments + 1;
		}else if(containsCosmeticVerbiage(comment)){
			cosmeticComments = cosmeticComments + 1;
		}else{
			miscComments = miscComments + 1;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	private void isDuplicate(String comment){
		if(allComments.contains(comment)){
			duplicateComments = duplicateComments + 1;
		}
	}
	
	public int getDuplicateComments(){
		return duplicateComments;
	}
	
	public int getPeformanceComments(){
		return peformanceComments;
	}
	
	public int getCosmeticComments(){
		return cosmeticComments;
	}

	public int getMiscComments(){
		return miscComments;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	private boolean containsPerfomanceVerbiage(String comment){
		for(String verbiage : helper.getPerformanceCommentsLookup()){
			if(comment.contains(verbiage)){
				return true;
			}
		}
		return false;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	private boolean containsCosmeticVerbiage(String comment){
		for(String verbiage : helper.getCosmeticCommentsLookup()){
			if(comment.contains(verbiage)){
				return true;
			}
		}
		return false;
	}
	
	public List<String> getAllComments(){
		return allComments;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isMemberOfReviewTeam(){
		return isMemberOfReviewTeam;
	}
	
	public void setMemberOfReviewTeam(boolean isMemberOfReviewTeam){
		this.isMemberOfReviewTeam = isMemberOfReviewTeam;
	}
}
