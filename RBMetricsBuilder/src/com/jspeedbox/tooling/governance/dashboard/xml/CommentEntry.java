package com.jspeedbox.tooling.governance.dashboard.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommentEntry {
	
	private String author = null;
	private String comment = null;
	
	public CommentEntry(){
		
	}

	@XmlAttribute
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@XmlAttribute
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
