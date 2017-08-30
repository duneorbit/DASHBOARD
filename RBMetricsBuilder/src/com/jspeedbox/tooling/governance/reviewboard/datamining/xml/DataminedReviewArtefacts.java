package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="DataminedReviewArtefacts")
public class DataminedReviewArtefacts {
	
	private String developer = null;
	
	private List<ReviewArtefact> reviewArtefacts = new ArrayList<ReviewArtefact>();
	
	public DataminedReviewArtefacts(){
		
	}

	public String getDeveloper() {
		return developer;
	}

	@XmlElement
	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public List<ReviewArtefact> getReviewArtefacts() {
		return reviewArtefacts;
	}

	@XmlElementWrapper
	@XmlElement(name="ReviewArtefact")
	public void setReviewArtefacts(List<ReviewArtefact> reviewArtefacts) {
		this.reviewArtefacts = reviewArtefacts;
	}
	
	public void addReviewArtefact(ReviewArtefact reviewArtefact){
		this.reviewArtefacts.add(reviewArtefact);
	}

}
