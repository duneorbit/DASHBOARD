package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

public class DataMineRequest {
	
	private boolean started = false;

	private String msg = null;
	private String dashboard = null;
	
	public DataMineRequest(){
		
	}

	public String getDashboard() {
		return dashboard;
	}

	public void setDashboard(String dashboard) {
		this.dashboard = dashboard;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
