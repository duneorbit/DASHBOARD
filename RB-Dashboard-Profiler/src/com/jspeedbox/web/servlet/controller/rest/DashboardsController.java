package com.jspeedbox.web.servlet.controller.rest;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jspeedbox.tooling.governance.reviewboard.DashboardCompiler;
import com.jspeedbox.tooling.governance.reviewboard.datamining.ProcessingStatus;
import com.jspeedbox.tooling.governance.reviewboard.datamining.Status;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.AdditionalSchedule;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.DataMineProcessStatus;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.DataMineRequest;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ProgammeIncrementDashboard;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.ScheduledJobsConfig;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.SprintCommentButtons;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboard;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.TeamDashboards;
import com.jspeedbox.tooling.governance.reviewboard.datamining.xml.Users;
import com.jspeedbox.utils.IOUtils;
import com.jspeedbox.utils.XMLUtils;
import com.jspeedbox.utils.json.JSONSyndicate;
import com.jspeedbox.utils.schedule.ScheduleManager;
import com.jspeedbox.utils.thread.DatamineMainThread;
import com.jspeedbox.web.servlet.Config;
import com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants;
import com.jspeedbox.web.servlet.controller.validator.ValidationException;

@Controller
public class DashboardsController {
	
	@RequestMapping(value = RestServiceConstants.CREATE_ADDITIONAL_SCHEDULE, method = RequestMethod.POST)
    public @ResponseBody AdditionalSchedule createAdditionalJobSchedule(@RequestBody AdditionalSchedule additionalSchedule) {
		try{
			if(ScheduleManager.jobExists(additionalSchedule.getDashboard(), additionalSchedule.getScheduleName())){
				additionalSchedule.setSuccess(false);
				additionalSchedule.setMsg("Schedule Job Exists!");
			}else{
				ScheduleManager.addJob(additionalSchedule);
			}
		}catch(Exception e){
			additionalSchedule.setSuccess(false);
			additionalSchedule.setMsg(e.getMessage());
		}
		
		additionalSchedule.setSuccess(true);
		
		return additionalSchedule;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_PI_SPRINT_CONFIG, method = RequestMethod.GET)
	public @ResponseBody ProgammeIncrementDashboard getPIDashboard(){
		ProgammeIncrementDashboard progammeIncrementDashboard = XMLUtils.unmarshallProgammeIncrementDashboard();
		progammeIncrementDashboard.sort();
		return progammeIncrementDashboard;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_METRICS_SUMMARY_BUTTONS, method = RequestMethod.GET)
	public @ResponseBody SprintCommentButtons getSprintButtons(@PathVariable("team") String dashboard) throws IOException {
		SprintCommentButtons buttons = null;
		ObjectMapper mapper = new ObjectMapper();
		buttons = mapper.readValue(IOUtils.readSprintButtonsJSON(dashboard), SprintCommentButtons.class);
		return buttons;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_RECOMPILE_TEAM_DASHBOARD, method = RequestMethod.GET)
	public @ResponseBody JSONSyndicate recompileDashboard(@PathVariable("dashboard") String dashboard) {
		JSONSyndicate syndicate = new JSONSyndicate();
		DashboardCompiler compiler = new DashboardCompiler();
		try{
			compiler.runWebCompiler(dashboard);
			syndicate.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			syndicate.setSuccess(false);
			String msg = "";
			if(compiler.isCompileError()){
				msg = compiler.getCompileErrorMsg();
			}else{
				msg = String.format(ValidationException.MSG_NO_DASHBOARD_ROOT, dashboard);
			}
			syndicate.setMsg(msg);
		}
		return syndicate;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_METRICS_SUMMARY_DATA, method = RequestMethod.GET)
	public @ResponseBody JSONSyndicate getMetricsSummaryTable(@PathVariable("team") String team) {
		JSONSyndicate syndicate = new JSONSyndicate();
		try{
			syndicate.setEncodedContent(IOUtils.readMetricsSummaryTableXML(team));
			syndicate.setSuccess(true);
		}catch(Exception e){
			syndicate.setSuccess(false);
			syndicate.setMsg(e.getMessage());
		}
		return syndicate;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_METRICS_REVIEWERS_DATA, method = RequestMethod.GET)
	public @ResponseBody JSONSyndicate getMetricsReviewersTable(@PathVariable("team") String team) {
		JSONSyndicate syndicate = new JSONSyndicate();
		try{
			syndicate.setEncodedContent(IOUtils.readMetricsReviewersTableXML(team));
			syndicate.setSuccess(true);
		}catch(Exception e){
			syndicate.setSuccess(false);
			syndicate.setMsg(e.getMessage());
		}
		return syndicate;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_DEVELOPERS_REVIEW_TABLE, method = RequestMethod.GET)
	public @ResponseBody JSONSyndicate getDevelopersReviewTable(@PathVariable("team") String team, @PathVariable("filename") String filename) {
		StringBuffer xmlsnippet = new StringBuffer();
		xmlsnippet.append(filename);
		if(!filename.endsWith(".xml")){
			xmlsnippet.append(".xml");
		}
		JSONSyndicate syndicate = new JSONSyndicate();
		
		try{
			syndicate.setEncodedContent(IOUtils.readDevelopersReviewTableXML(team, xmlsnippet.toString()));
			syndicate.setSuccess(true);
		}catch(Exception e){
			syndicate.setSuccess(false);
			syndicate.setMsg(e.getMessage());
		}
		
		return syndicate;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_INDEXING_DATA, method = RequestMethod.GET)
	public void getIndexingData(HttpServletResponse response, @PathVariable("team") String team) {
		try{
			File file = IOUtils.readIndexingDataFile(team);
			response.setContentType(IOUtils.APPLICATION_JAVASCRIPT);
			response.setContentLength(IOUtils.getContentLength(file));
			IOUtils.copy(file, response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = RestServiceConstants.GET_PRESENTATION_GRAPHS_DATA, method = RequestMethod.GET)
	public void getPresentationGraphData(HttpServletResponse response, @PathVariable("team") String team) {
		try{
			File file = IOUtils.readPresentationGraphs(team);
			response.setContentType(IOUtils.APPLICATION_JAVASCRIPT);
			response.setContentLength(IOUtils.getContentLength(file));
			IOUtils.copy(file, response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = RestServiceConstants.CREATE_PI_SPRINT_CONFIG, method = RequestMethod.POST)
    public @ResponseBody JSONSyndicate createPISprintDashboard(@RequestBody ProgammeIncrementDashboard wrapper) {
		JSONSyndicate syndicate = new JSONSyndicate();
		
		try{
			XMLUtils.saveXMLDocument(wrapper, ProgammeIncrementDashboard.class, IOUtils.getPISprintDatesConigXML());
			syndicate.setSuccess(true);
		}catch(Exception e){
			syndicate.setSuccess(false);
			syndicate.setMsg(e.getMessage());
		}
		
		return syndicate;
	}
	
	@RequestMapping(value = RestServiceConstants.DELETE_TEAM_DASHBOARD, method = RequestMethod.GET)
	public @ResponseBody TeamDashboard deleteTeamDashboard(@PathVariable("dashboard") String dashboard) {
		TeamDashboards dashboards = XMLUtils.unmarshallTeamDashboards();
		ListIterator<TeamDashboard> dashboardsItr = dashboards.getDashboards().listIterator();
		while(dashboardsItr.hasNext()){
			if(dashboardsItr.next().getDashboardName().equalsIgnoreCase(dashboard)){
				dashboardsItr.remove();
			}
		}
		
		TeamDashboard teamDashboard = new TeamDashboard();
		
		try{
			XMLUtils.saveXMLDocument(dashboards, TeamDashboards.class, IOUtils.getDashBoardsSummaryXml());
			teamDashboard.setSuccess(true);
		}catch(Exception e){
			teamDashboard.setSuccess(false);
			teamDashboard.setMsg(e.getMessage());
		}
		return teamDashboard;
	}
	@SuppressWarnings("static-access")
	@RequestMapping(value = RestServiceConstants.START_TEAM_DASHBOARD_DATAMINE, method = RequestMethod.POST)
    public @ResponseBody DataMineRequest startDataMineProcess(@RequestBody DataMineRequest dataMineRequest) {
		ProcessingStatus.getInstance().setInitiateProcessInvoked(true);
		ExecutorService pool = Executors.newFixedThreadPool(1);
		DatamineMainThread dataMinerThread = new DatamineMainThread(dataMineRequest.getDashboard());
		pool.execute(dataMinerThread);
		dataMineRequest.setStarted(true);
		return dataMineRequest;
	}
	
	@RequestMapping(value = RestServiceConstants.GET_TEAM_DASHBOARD_COMPILE, method = RequestMethod.GET)
	public @ResponseBody Status getCompilerProcess(@PathVariable("dashboard") String dashboard) {
		return ProcessingStatus.getInstance().getCompileStatus(dashboard);
	}
	
	@RequestMapping(value = RestServiceConstants.GET_TEAM_DASHBOARD_DATAMINE, method = RequestMethod.GET)
	public @ResponseBody DataMineProcessStatus getDataMineProcess(@PathVariable("dashboard") String dashboard) {
		//look to see if the process is running
		return ProcessingStatus.getInstance().getStatus(dashboard);
	}
	
	@RequestMapping(value = RestServiceConstants.GET_TEAM_DASHBOARD, method = RequestMethod.GET)
	public @ResponseBody TeamDashboard getTeamDashboard(@PathVariable("dashboard") String dashboard) {
		return XMLUtils.getTeamDashboard(dashboard);
	}
	
	@RequestMapping(value = RestServiceConstants.GET_ALL_USERS, method = RequestMethod.GET)
	public @ResponseBody Users getUserList() {
		Users users = new Users();
		users.getUsers().addAll(Config.getInstance().getUserList());
		return users;
	}
	
	@RequestMapping(value = RestServiceConstants.ALL_TEAM_DASHBOARD, method = RequestMethod.GET)
	public @ResponseBody TeamDashboards getTeamDashboards() {
		return XMLUtils.unmarshallTeamDashboards();
	}
	
	@RequestMapping(value = RestServiceConstants.CREATE_TEAM_DASHBOARD, method = RequestMethod.POST)
    public @ResponseBody TeamDashboard createTeamDashboard(@RequestBody TeamDashboard teamDashboard) {
		
		boolean newDashbard = false;
		
		if(!IOUtils.getDashBoardsSummaryXml().exists()){
			newDashbard = true;
		}
		
		try{
			teamDashboard = XMLUtils.createDashBoardsSummary(teamDashboard, newDashbard);
		
			try{
				IOUtils.getDashBoardRoot(teamDashboard.getDashboardName());
			}catch(Exception e){
				boolean created = IOUtils.createDashboardRoot(teamDashboard.getDashboardName());
				if(!created){
					teamDashboard.setSuccess(false);
					teamDashboard.setMsg(e.getMessage());
				}
			}
			
			ScheduledJobsConfig jobsConfig = XMLUtils.getScheduledJobs();
			if(jobsConfig==null){
				jobsConfig = new ScheduledJobsConfig();
			}
			
			ScheduledJobConfig scheduledJob = new ScheduledJobConfig();
			jobsConfig.getJobs().add(scheduledJob);
			
			scheduledJob.setGroup(teamDashboard.getDashboardName());
			scheduledJob.setName(teamDashboard.getDashboardName());
			
			XMLUtils.saveXMLDocument(jobsConfig, ScheduledJobsConfig.class, IOUtils.getScheduledJobsConfigXML(true));
			
			ScheduleManager.addJob(scheduledJob.getGroup(), scheduledJob.getScheduleName());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return teamDashboard;
	}
	
	@RequestMapping(value = RestServiceConstants.UPDATE_TEAM_DASHBOARD, method = RequestMethod.POST)
    public @ResponseBody TeamDashboard updateTeamDashboard(@RequestBody TeamDashboard teamDashboard) {
		
		TeamDashboards dashboards = XMLUtils.unmarshallTeamDashboards();
		ListIterator<TeamDashboard> dashboardsItr = dashboards.getDashboards().listIterator();
		
		while(dashboardsItr.hasNext()){
			if(dashboardsItr.next().getDashboardName().equalsIgnoreCase(teamDashboard.getDashboardName())){
				dashboardsItr.remove();
			}
		}
		dashboards.getDashboards().add(teamDashboard);
		
		try{
			XMLUtils.saveXMLDocument(dashboards, TeamDashboards.class, IOUtils.getDashBoardsSummaryXml());
			teamDashboard.setSuccess(true);
		}catch(Exception e){
			teamDashboard.setSuccess(false);
			teamDashboard.setMsg(e.getMessage());
		}
		return teamDashboard;
	}

}
