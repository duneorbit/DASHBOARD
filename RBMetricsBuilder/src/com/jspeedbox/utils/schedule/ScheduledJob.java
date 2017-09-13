package com.jspeedbox.utils.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.datamining.DataMineThreadProcessor;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.utils.logging.LoggingUtils;

public class ScheduledJob implements Job{
	
	private static final String SCHEDULER = "Scheduler";
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(ScheduledJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		String dashboard = (String)context.getJobDetail().getJobDataMap().get(IOUtils.KEY_DASHBOARD);
		String jobname = (String)context.getJobDetail().getJobDataMap().get(IOUtils.KEY_JOBNAME);
		
		LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "This job is kicking off for dashboard"), "execute", dashboard);
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
			LOGGER_.error("Method[{}] Exception[{}] ", "execute", e);
		}
	}

}
