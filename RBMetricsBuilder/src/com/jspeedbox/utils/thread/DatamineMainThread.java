package com.jspeedbox.utils.thread;

import com.jspeedbox.tooling.governance.reviewboard.datamining.DataMineThreadProcessor;

public class DatamineMainThread implements Runnable{
	
	private String dashboard = null;
	
	public DatamineMainThread(String dashboard){
		this.dashboard = dashboard;
	}

	@Override
	public void run() {
		try{
			DataMineThreadProcessor.getInstance().init(this.dashboard);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
