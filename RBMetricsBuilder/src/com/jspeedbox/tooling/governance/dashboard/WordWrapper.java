package com.jspeedbox.tooling.governance.dashboard;

import java.util.ArrayList;
import java.util.List;

public abstract class WordWrapper {
	
	protected List<CloudWord> words = new ArrayList<CloudWord>();
	
	protected void addCloudWord(CloudWord word){
		words.add(word);
	}

	public List<CloudWord> getWords() {
		return words;
	}
	
}
