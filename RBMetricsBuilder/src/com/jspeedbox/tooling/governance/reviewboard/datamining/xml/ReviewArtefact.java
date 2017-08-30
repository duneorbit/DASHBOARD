package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ReviewArtefacts")
public class ReviewArtefact {
	
	private List<Revision> revisionHistory = new ArrayList<Revision>();
	
	public ReviewArtefact(){
		
	}

	public List<Revision> getRevisionHistory() {
		return revisionHistory;
	}

	@XmlElementWrapper
	@XmlElement(name="RevisionHistory")
	public void setRevisionHistory(List<Revision> revisionHistory) {
		this.revisionHistory = revisionHistory;
	}
	
	public void addRevisionHistory(Revision revision){
		this.revisionHistory.add(revision);
	}

}
