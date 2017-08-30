package com.jspeedbox.utils.schedule;

import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.AdditionalSchedule;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.web.servlet.Config;

public class ScheduleManager {
	
	private static boolean STARTED = false;
	
	private static ScheduleManager INSTANCE = null;
	
	private static Scheduler sheduler = null;
	
	private ScheduleManager() throws SchedulerException{
		start();
		loadJobs();
	}
	
	public static synchronized ScheduleManager getInstance() throws SchedulerException{
		if(INSTANCE==null){
			INSTANCE = new ScheduleManager();
		}
		return INSTANCE;
	}
	
	private static void start() throws SchedulerException{
		System.out.println("Starting Up");
		sheduler = new StdSchedulerFactory().getScheduler();
		sheduler.start();
	}
	
	public static void shutdown() throws SchedulerException{
		if(!sheduler.isShutdown()){
			System.out.println("Shutting Down");
			sheduler.shutdown();
		}
	}
	
	public static void loadJobs() throws SchedulerException{
		ScheduledJobsConfig scheduledJobs = XMLUtils.getScheduledJobs();
		if(scheduledJobs!=null){
			for(ScheduledJobConfig configJob : scheduledJobs.getJobs()){
				addJob(configJob.getGroup(), configJob.getScheduleName());
				if(configJob.getAdditionalSchedule()!=null){
					addJob(configJob.getAdditionalSchedule());
				}
			}
		}
	}
	
	public static boolean jobExists(String dashboard, String jobName) throws SchedulerException{
		Set<JobKey> jobKeys = sheduler.getJobKeys(GroupMatcher.jobGroupEquals(dashboard));
		for(JobKey job : jobKeys){
			if(job.getName().equals(jobName)){
				return true;
			}
		}
		return false;
	}
	
	private static String getTriggerName(String jobname){
		StringBuffer buffer = new StringBuffer();
		buffer.append("trigger-").append(jobname);
		return buffer.toString();
	}
	
	private static void addJob(String group, String jobname, String period) throws SchedulerException{
		JobKey jobkey = new JobKey(jobname, group);
		JobDetail detail = JobBuilder.newJob(ScheduledJob.class).withIdentity(jobkey).build();
		detail.getJobDataMap().put(IOUtils.KEY_DASHBOARD, group);
		detail.getJobDataMap().put(IOUtils.KEY_JOBNAME, jobname);
		Trigger trigger = (Trigger)TriggerBuilder.newTrigger().withIdentity(
				getTriggerName(jobname), group).withSchedule(CronScheduleBuilder.cronSchedule(period)).build();
		sheduler.scheduleJob(detail, trigger);
	}
	
	public static synchronized void addJob(String dashboard, String jobname) throws SchedulerException{
		addJob(dashboard, jobname, Config.getInstance().getParam(IOUtils.KEY_NIGHTLY));
	}
	
	public static synchronized void addJob(AdditionalSchedule additionalSchedule) throws SchedulerException{
		ScheduledJobsConfig scheduledJobs = XMLUtils.getScheduledJobs();
		if(scheduledJobs==null){
			throw new SchedulerException("no Scheduled Jobs Config XML found");
		}
		for(ScheduledJobConfig configJob : scheduledJobs.getJobs()){
			configJob.setAdditionalSchedule(additionalSchedule);
		}
		try{
			XMLUtils.saveXMLDocument(scheduledJobs, ScheduledJobsConfig.class, IOUtils.getScheduledJobsConfigXML(true));
			addJob(additionalSchedule.getDashboard(), additionalSchedule.getScheduleName(), additionalSchedule.getPattern());
		}catch(Exception e){
			
		}
	}

	public static boolean isSTARTED() {
		return STARTED;
	}

	private static void setSTARTED(boolean sTARTED) {
		STARTED = sTARTED;
	}
	
	

}
