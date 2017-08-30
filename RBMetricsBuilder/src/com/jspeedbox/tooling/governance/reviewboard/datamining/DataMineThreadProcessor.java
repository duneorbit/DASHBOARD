package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.jspeedbox.tooling.governance.reviewboard.DashboardCompiler;
import com.jspeedbox.tooling.governance.reviewboard.User;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboard;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;

public class DataMineThreadProcessor {
	
	private static final int MAX_THREADS = 10;
	
	private static boolean error = false;
	private static boolean running = false;
	
	private static String message = null;
	private static String runningDashboard = null;
	
	private static DataMineThreadProcessor INSTANCE_ = null;
	
	private static LinkedList<String> QUEUE = new LinkedList<String>();
	
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
				
				//wait for threads to become active
				//TODO make more robust
				pause();
				
				boolean notCompleted = true;
				while(notCompleted){
					Set<Thread> threads = Thread.getAllStackTraces().keySet();
					boolean runningThreads = false;
					for(Thread thread : threads){
						//System.out.println("thread name["+thread.getName()+"]");
						if(thread.getName().contains("DataMineService")){
							//System.out.println("state["+thread.getState().name()+"]");
							if(thread.getState().name().equals(State.RUNNABLE.toString())){
								runningThreads = true;
							}
						}
					}
					notCompleted = runningThreads;
				}
				
				System.out.println("................ compiling dashboard");
				//compile dashboard
				DashboardCompiler compiler = new DashboardCompiler();
				compiler.runWebCompiler(runningDashboard);
				
				System.out.println("compiled");
				synchronized(QUEUE){
					QUEUE.remove(runningDashboard);
				}
				
				printQueue();
				
				ProcessingStatus.getInstance().finished();
				
			}catch(Exception e){
				error = true;
				message = e.getMessage();
				e.printStackTrace();
			}
		}
	}
	
	private static void printQueue(){
		System.out.println("Dashboards left in queue");
		synchronized(QUEUE){
			if(QUEUE.size()>0){
				for(String dashboard : QUEUE){
					System.out.println(dashboard);
				}
			}else{
				System.out.println("none");
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void pause() throws InterruptedException{
		while(ProcessingStatus.getInstance().isStartThreadPoolMonitoring()==false){
			
		}
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
