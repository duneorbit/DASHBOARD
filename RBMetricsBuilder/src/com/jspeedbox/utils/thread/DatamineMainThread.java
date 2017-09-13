package com.jspeedbox.utils.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.datamining.DataMineThreadProcessor;

public class DatamineMainThread implements Runnable{
	
	private String dashboard = null;
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(DatamineMainThread.class);
	
	public DatamineMainThread(String dashboard){
		this.dashboard = dashboard;
	}

	@Override
	public void run() {
		try{
			DataMineThreadProcessor.getInstance().init(this.dashboard);
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "run", e);
		}
		
	}

}
