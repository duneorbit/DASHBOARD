package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.jspeedbox.tooling.governance.reviewboard.datamining.Status;

public class DataMineProcessStatus {
	
	private String msg = null;
	
	private int overAllPerc = 0;
	
	private Map<String, Status> status = new HashMap<String, Status>();
	
	public DataMineProcessStatus(){
		
	}
	
	public int getOverAllPerc(){
		return overAllPerc;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Status> getStatus() {
		return status;
	}

	public void setStatus(Map<String, Status> status) {
		this.status = status;
	}
	
	public void overAllProgress(){
		int completed = addCompleted();
		if(completed>0){
			overAllPerc = ((addCompleted()*100)/addAll());
		}
	}
	
	public void compileProgress(){
		
	}
	
	private int addCompleted(){
		int completed = 0;
		Iterator<Entry<String, Status>> entryItr = status.entrySet().iterator();
		while(entryItr.hasNext()){
			Entry<String, Status> entry = entryItr.next();
			completed = completed + entry.getValue().getComplete();
		}
		return completed;
	}
	
	private int addAll(){
		int all = 0;
		Iterator<Entry<String, Status>> entryItr = status.entrySet().iterator();
		while(entryItr.hasNext()){
			Entry<String, Status> entry = entryItr.next();
			all = all + entry.getValue().getAll();
		}
		return all;
	}

}
