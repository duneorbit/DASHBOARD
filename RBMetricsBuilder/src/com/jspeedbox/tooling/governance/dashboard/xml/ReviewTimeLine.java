package com.jspeedbox.tooling.governance.dashboard.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.jspeedbox.utils.DateUtils;

@XmlRootElement
public class ReviewTimeLine implements Comparable<ReviewTimeLine>{
	
	String summary = null;
	String lastActivityDate = null;
	
	int reviewNum = 0;
	
	private Date parsedDate = null;
	
	private List<TimeLine> timeline = new ArrayList<TimeLine>();
	
	public ReviewTimeLine(){
		
	}
	
	public ReviewTimeLine(int reviewNum){
		this.reviewNum = reviewNum;
	}

	@XmlAttribute
	public int getReviewNum() {
		return reviewNum;
	}

	public void setReviewNum(int reviewNum) {
		this.reviewNum = reviewNum;
	}

	public List<TimeLine> getTimeline() {
		return timeline;
	}

	public void setTimeline(List<TimeLine> timeline) {
		this.timeline = timeline;
	}

	@XmlAttribute
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@XmlAttribute
	public String getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(String lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
		parsedDate = DateUtils.formatTimestampSlashDate(lastActivityDate);
	}
	
	public Date getParsedDate(){
		return parsedDate;
	}

	@Override
	public int compareTo(ReviewTimeLine in) {
		// TODO Auto-generated method stub
		return getParsedDate().compareTo(in.getParsedDate());
	}
	
	

}
