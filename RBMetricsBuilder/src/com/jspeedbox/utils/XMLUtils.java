package com.jspeedbox.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ProgammeIncrementDashboard;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboard;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboards;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Users;
import com.jspeedbox.utils.logging.LoggingUtils;

public class XMLUtils {
	
	private static Logger LOGGER_ = LoggerFactory.getLogger(XMLUtils.class);
	
	@SuppressWarnings("rawtypes")
	public static void saveXMLDocument(Object xmlObject, Class clazz, File file) throws Exception{
		try{
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller marshaller = context.createMarshaller();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			marshaller.marshal(xmlObject, stream);
			
			LOGGER_.debug(LoggingUtils.buildParamsPlaceHolders("method", "Saving xml document"), "saveXMLDocument", file.getAbsolutePath());
			
			synchronized(file){
				FileUtils.writeByteArrayToFile(file, stream.toByteArray());
			}
			
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "saveXMLDocument", e);
			throw e;
		}
	}
	
	public static ProgammeIncrementDashboard unmarshallProgammeIncrementDashboard(){
		ProgammeIncrementDashboard progammeIncrementDashboard = new ProgammeIncrementDashboard();
		try{
			JAXBContext context = JAXBContext.newInstance(ProgammeIncrementDashboard.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			progammeIncrementDashboard = (ProgammeIncrementDashboard)unmarshaller.unmarshal(IOUtils.getPISprintDatesConigXML());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "unmarshallProgammeIncrementDashboard", e);
		}
		return progammeIncrementDashboard;
	}
	
	public static Users unmarshallUsers(){
		Users users = new Users();
		try{
			JAXBContext context = JAXBContext.newInstance(Users.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			users = (Users)unmarshaller.unmarshal(IOUtils.getRbUsersXML());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "unmarshallUsers", e);
		}
		return users;
	}
	
	public static TeamDashboards unmarshallTeamDashboards(){
		TeamDashboards dashboards = new TeamDashboards();
		try{
			JAXBContext context = JAXBContext.newInstance(TeamDashboards.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			dashboards = (TeamDashboards)unmarshaller.unmarshal(IOUtils.getDashBoardsSummaryXml());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "unmarshallTeamDashboards", e);
		}
		return dashboards;
	}
	
	public static TeamDashboard getTeamDashboard(String dashboard){
		TeamDashboards dashboards = unmarshallTeamDashboards();
		if(dashboards!=null){
			for(TeamDashboard currDashboard : dashboards.getDashboards()){
				if(currDashboard.getDashboardName().equalsIgnoreCase(dashboard)){
					return currDashboard;
				}
			}
		}
		return new TeamDashboard();
	}
	
	public static TeamDashboard createDashBoardsSummary(TeamDashboard teamboard, boolean create) throws Exception{
		TeamDashboards dashboards = null;
		if(create){
			dashboards = new TeamDashboards();
		}else{
			dashboards = unmarshallTeamDashboards();
		}
		for(TeamDashboard created : dashboards.getDashboards()){
			if(created.getDashboardName().equalsIgnoreCase(teamboard.getDashboardName())){
				teamboard.setSuccess(false);
				teamboard.setMsg("Dashboard with this name exists");
				return teamboard;
			}
		}
		dashboards.getDashboards().add(teamboard);
		saveXMLDocument(dashboards, TeamDashboards.class, IOUtils.getDashBoardsSummaryXml());
		teamboard.setSuccess(true);
		return teamboard;
	}
	
	public static ProgammeIncrementDashboard getPISprintDashboard(){
		ProgammeIncrementDashboard piSprintDashboard = null;
		try{
			JAXBContext context = JAXBContext.newInstance(ProgammeIncrementDashboard.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			piSprintDashboard = (ProgammeIncrementDashboard)unmarshaller.unmarshal(IOUtils.getPISprintDatesConigXML());
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "getPISprintDashboard", e);
		}
		return piSprintDashboard;
	}
	
	public static ScheduledJobsConfig getScheduledJobs(){
		ScheduledJobsConfig scheduledJobs = null;
		try{
			JAXBContext context = JAXBContext.newInstance(ScheduledJobsConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			scheduledJobs = (ScheduledJobsConfig)unmarshaller.unmarshal(IOUtils.getScheduledJobsConfigXML(false));
		}catch(Exception e){
			LOGGER_.error("Method[{}] Exception[{}] ", "getScheduledJobs", e);
		}
		return scheduledJobs;
	}

}
