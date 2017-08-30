package com.jspeedbox.tooling.governance.dashboard;

import java.util.LinkedList;

public class CommitSessionItem {
	
	private LinkedList<CommitSessionFile> commitSessionFiles = new LinkedList<CommitSessionFile>();
	
	public CommitSessionItem(){
		
	}
	
	public LinkedList<CommitSessionFile> getCommitSessionFiles(){
		return commitSessionFiles;
	}

}
