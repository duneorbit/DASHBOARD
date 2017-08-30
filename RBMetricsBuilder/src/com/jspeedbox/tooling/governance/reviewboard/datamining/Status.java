package com.jspeedbox.tooling.governance.reviewboard.datamining;

public class Status {
	
	private boolean invalid = false;
	private boolean finished = false;
	private boolean started = false;
	
	private String username = null;
	
	private int complete = 0;
	private int all = 0;
	private int pec = 0;
	
	public Status(){
		
	}
	
	public Status(boolean finished){
		setFinished(finished);
	}
	
	public Status(String user){
		this.username = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getComplete() {
		return complete;
	}

	public void incComplete() {
		this.complete = this.complete + 1;
		calPerc();
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}
	
	private void calPerc(){
		pec = ((complete*100)/all);
	}
	
	public int getPec(){
		return pec;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
	
	

}
