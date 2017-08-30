package com.jspeedbox.tooling.governance.dashboard;

public class CloudWord {
	
	private String text = null;
	private String weight = null;
	
	public CloudWord(String text, String weight){
		this.text = text;
		this.weight = weight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	
}
