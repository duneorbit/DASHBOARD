package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Revision")
public class Revision {
	
	private String revisionNum = null;

	private String revisionJSON = null;
	
	public Revision(){
		
	}

	public String getRevisionJSON() {
		return revisionJSON;
	}

	@XmlElement
	public void setRevisionJSON(String revisionJSON) {
		this.revisionJSON = revisionJSON;
	}
	
	public String getRevisionNum() {
		return revisionNum;
	}

	@XmlElement
	public void setRevisionNum(String revisionNum) {
		this.revisionNum = revisionNum;
	}

}
