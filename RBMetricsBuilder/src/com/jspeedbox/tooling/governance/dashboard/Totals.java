package com.jspeedbox.tooling.governance.dashboard;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
public class Totals {
	
	private boolean isDeveloperReply = false;
	
	private String developer = null;
	
	private int cosmeticComments = 0;
	private int duplicateComments;
	private int miscComments = 0;
	private int peformanceComments = 0;
	private int totalFiles = 0;
	private int total = 0;
	
	public Totals(){
		
	}
	
	public Totals(String developer){
		this.developer = developer;
	}
	
	public void updateTotals(Reviewer reviewer){
		addCos(reviewer.getCosmeticComments());
		addDup(reviewer.getDuplicateComments());
		addMisc(reviewer.getMiscComments());
		addPerf(reviewer.getPeformanceComments());
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addCos(int cos){
		cosmeticComments = cosmeticComments + cos;
		//total = total + cos;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addDup(int dup){
		duplicateComments = duplicateComments + dup;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addMisc(int misc){
		miscComments = miscComments + misc;
		//total = total + misc;
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true) 
	public void addPerf(int perf){
		peformanceComments = peformanceComments + perf;
		//total = total + perf;
	}

	@XmlAttribute
	public int getCosmeticComments() {
		return cosmeticComments;
	}

	@XmlAttribute
	public int getDuplicateComments() {
		return duplicateComments;
	}

	@XmlAttribute
	public int getMiscComments() {
		return miscComments;
	}

	@XmlAttribute
	public int getPeformanceComments() {
		return peformanceComments;
	}

	@XmlAttribute
	public int getTotal() {
		return total;
	}
	
	public void incTotal(){
		this.total = this.total + 1;
	}
	
	public void incTotalFiles(){
		this.totalFiles = this.totalFiles + 1;
	}

	@XmlAttribute
	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	@XmlAttribute
	public boolean isDeveloperReply() {
		return isDeveloperReply;
	}

	public void setDeveloperReply(boolean isDveloperReply) {
		this.isDeveloperReply = isDveloperReply;
	}

	public void updateTotal(int total){
		this.total = this.total + total;
	}
}
