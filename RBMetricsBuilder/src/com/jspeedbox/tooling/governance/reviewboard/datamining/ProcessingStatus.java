package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.DataMineProcessStatus;

public class ProcessingStatus {
	
	private boolean startThreadPoolMonitoring = false;
	private boolean initiateProcessInvoked = false;
	
	private static String processingDashboard = null;
	
	private static ProcessingStatus INSTANCE_ = null;
	
	private Map<String, Status> datamineStatus = new HashMap<String, Status>();
	
	private Status compileStatus = new Status();
	
	private ProcessingStatus(){
		
	}
	
	public static synchronized ProcessingStatus getInstance(){
		if(INSTANCE_==null){
			INSTANCE_ = new ProcessingStatus();
		}
		return INSTANCE_;
	}
	
	public void reset(String runningDashboard, List<User> users){
		processingDashboard = runningDashboard;
		synchronized(datamineStatus){
			datamineStatus.clear();
			for(User user : users){
				add(user.getUsername());
			}
		}
		synchronized(compileStatus){
			compileStatus = new Status();
		}
	}
	
	public synchronized Status getCompileStatus(){
		return compileStatus;
	}
	
	public Status getCompileStatus(String checkDashboard){
		if(!checkDashboard.equalsIgnoreCase(processingDashboard)){
			compileStatus.setInvalid(true);
		}
		return compileStatus;
	}
	
	public void add(int total){
		compileStatus.setAll(total);
	}
	
	private void add(String user){
		if(!datamineStatus.containsKey(user)){
			datamineStatus.put(user, new Status(user));
		}
	}
	
	public synchronized Map<String, Status> getStatus(){
		return datamineStatus;
	}

	public String getProcessingDashboard() {
		return processingDashboard;
	}

	public static void setProcessingDashboard(String dashboard) {
		processingDashboard = dashboard;
	}
	
	public synchronized DataMineProcessStatus getStatus(String dashboard){
		DataMineProcessStatus proecessStatus = new DataMineProcessStatus();
		if(processingDashboard!=null){
			if(processingDashboard.equals(dashboard) || DataMineThreadProcessor.getInstance().isQueued(dashboard)){
				proecessStatus.setStatus(datamineStatus);
			}else{
				proecessStatus.setMsg("This dashboard is not currently running or queued to run");
			}
			proecessStatus.overAllProgress();
			proecessStatus.compileProgress();
		}else if(DataMineThreadProcessor.getInstance().isRuning()||isInitiateProcessInvoked()){
			proecessStatus.setMsg("retry");
		}
		return proecessStatus;
	}
	
	public void finished(){
		synchronized(compileStatus){
			compileStatus = new Status(true);
		}
		synchronized(datamineStatus){
			datamineStatus = new HashMap<String, Status>();
		}
	}

	public boolean isStartThreadPoolMonitoring() {
		return startThreadPoolMonitoring;
	}

	public void setStartThreadPoolMonitoring(boolean startThreadPoolMonitoring) {
		this.startThreadPoolMonitoring = startThreadPoolMonitoring;
	}

	public boolean isInitiateProcessInvoked() {
		return initiateProcessInvoked;
	}

	public void setInitiateProcessInvoked(boolean initiateProcessInvoked) {
		this.initiateProcessInvoked = initiateProcessInvoked;
	}
	
	

}
