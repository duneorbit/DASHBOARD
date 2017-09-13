package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.DashboardCompiler;
import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboard;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.utils.logging.LoggingUtils;

public class DataMineThreadProcessor {
	
	private static final int MAX_THREADS = 10;
	
	private static boolean error = false;
	private static boolean running = false;
	
	private static String message = null;
	private static String runningDashboard = null;
	
	private static DataMineThreadProcessor INSTANCE_ = null;
	
	private static LinkedList<String> QUEUE = new LinkedList<String>();
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(DataMineThreadProcessor.class);
	
	private DataMineThreadProcessor(){
		
	}
	
	public static synchronized DataMineThreadProcessor getInstance(){
		if(INSTANCE_==null){
			INSTANCE_ = new DataMineThreadProcessor();
		}
		return INSTANCE_;
	}
	
	public boolean isRuning(){
		return running;
	}
	
	public synchronized void init(String dashboard) throws IOException{
		if(!QUEUE.contains(dashboard)){
			QUEUE.add(dashboard);
		}
		if(!isRuning()){
			running = true;
			invokeDashboardProcessor();
		}
	}
	
	private static void flagAsStopped(){
		running = false;
	}
	
	private void invokeDashboardProcessor() throws IOException{
		while(QUEUE.size()>0){
			start();
		}
		flagAsStopped();
	}
	
	private void start() throws IOException{
		if(QUEUE.iterator().hasNext()){
			
			runningDashboard = QUEUE.iterator().next();
			try{
				IOUtils.compressFolder(IOUtils.getDataMinePath(runningDashboard), IOUtils.getDataMineArchivedPath(runningDashboard));
				FileUtils.cleanDirectory(IOUtils.getDataMinePath(runningDashboard));
				
				TeamDashboard dashboard = XMLUtils.getTeamDashboard(runningDashboard);
				ProcessingStatus.getInstance().reset(runningDashboard, dashboard.getUsers());
				
				ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS, new DataMineThreadFactory());
				
				//datamine dashboard users
				for(User user : dashboard.getUsers()){
					Runnable dataMinerThread = new ReviewBoardDataMineService(user.getUsername(), runningDashboard);
					pool.execute(dataMinerThread);
				}
				
				
				pause();
				
				boolean notCompleted = true;
				while(notCompleted){
					Set<Thread> threads = Thread.getAllStackTraces().keySet();
					boolean runningThreads = false;
					for(Thread thread : threads){
						if(thread.getName().contains("DataMineService")){
							if(thread.getState().name().equals(State.RUNNABLE.toString())){
								runningThreads = true;
							}
						}
					}
					notCompleted = runningThreads;
				}
				
				LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "compiling dashboard"), "start", runningDashboard);
				//compile dashboard
				DashboardCompiler compiler = new DashboardCompiler();
				compiler.runWebCompiler(runningDashboard);
				
				
				LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "compiled"), "start", runningDashboard);
				synchronized(QUEUE){
					QUEUE.remove(runningDashboard);
				}
				
				printQueue();
				
				ProcessingStatus.getInstance().finished();
				
			}catch(Exception e){
				error = true;
				message = e.getMessage();
				LOGGER_.error("Method[{}] Exception[{}] ", "start", e);
			}
		}
	}
	
	private static void printQueue(){
		StringBuffer printQueue = new StringBuffer();
		synchronized(QUEUE){
			if(QUEUE.size()>0){
				for(String dashboard : QUEUE){
					printQueue.append(dashboard).append(" , ");
				}
			}
		}
		LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "dashboards left in QUEUE"), "printQueue", printQueue.toString());
		printQueue = null;
	}
	
	private void pause() throws InterruptedException{
		while(ProcessingStatus.getInstance().isStartThreadPoolMonitoring()==false){}
	}
	
	public static boolean isError() {
		return error;
	}

	public static String getMessage() {
		return message;
	}

	public boolean isQueued(String dashboard){
		return QUEUE.contains(dashboard);
	}

}
