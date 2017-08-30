package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

public class ReviewSearchCriteria {
	
	private String startDate = null;
	private String url = null;
	
	public ReviewSearchCriteria(String url, String startDate){
		this.url = url;
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
