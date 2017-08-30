package com.jspeedbox.utils.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jspeedbox.tooling.governance.reviewboard.datamining.DataMineThreadProcessor;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;

public class ScheduledJob implements Job{
	
	private static final String SCHEDULER = "Scheduler";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String dashboard = (String)context.getJobDetail().getJobDataMap().get(IOUtils.KEY_DASHBOARD);
		String jobname = (String)context.getJobDetail().getJobDataMap().get(IOUtils.KEY_JOBNAME);
		
		System.out.println("This job is kicking off for dashboard["+dashboard+"]");
		try{
			DataMineThreadProcessor.getInstance().init(dashboard);
			
			ScheduledJobsConfig jobsConfig = XMLUtils.getScheduledJobs();
			for(ScheduledJobConfig jobConfig : jobsConfig.getJobs()){
				if(jobConfig.getScheduleName().equalsIgnoreCase(jobname)){
					jobConfig.setLastRunTime(System.currentTimeMillis());
					jobConfig.setLastRunBy(SCHEDULER);
					break;
				}
				if(jobConfig.getAdditionalSchedule().getScheduleName().equalsIgnoreCase(jobname)){
					jobConfig.getAdditionalSchedule().setLastRunTime(System.currentTimeMillis());
					jobConfig.getAdditionalSchedule().setLastRunBy(SCHEDULER);
					break;
				}
			}
			
			XMLUtils.saveXMLDocument(jobsConfig, ScheduledJobsConfig.class, IOUtils.getScheduledJobsConfigXML(true));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
