package com.jspeedbox.tooling.governance.dashboard;

public class CommitSessionFile {
	
	private String file = null;
	private String activity = null;
	
	public CommitSessionFile(String file, String activity){
		this.file = file;
		this.activity = activity;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	

}
