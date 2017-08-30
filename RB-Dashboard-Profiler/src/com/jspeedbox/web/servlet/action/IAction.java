package com.jspeedbox.web.servlet.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAction {
	
	public static final String ACTION_BASE = "com.jspeedbox.web.servlet.action.";
	public static final String ACTION_USER_BUILD_ACTION = "users.UserBuildAction";
	
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_DASHBOARD = "dashboard";
	
	public static final String REQUEST_ATTRIBUTE_USERS_XML = "usersXML";
	public static final String REQUEST_ATTRIBUTE_TEAMS_DASHBOARD_XML = "dashBoardsSummaryXml";
	public static final String REQUEST_ATTRIBUTE_PI_SPRINT_DATES = "piSprintDatesConfigXml";
	public static final String REQUEST_ATTRIBUTE_NO_DASHBOARD_ROOT = "noDashboardRoot";
	
	public static final String JSP = ".jsp";
	public static final String JSP_NAME_VIEW_DAHSBOARD = "viewDashboard.jsp";
	public static final String JSP_PROFILER = "./profiler.jsp";
	public static final String JSP_BUILD_TEAM_SAHBOARD = "./buildTeamDashboard.jsp";
	public static final String JSP_CREATE_PI_SPRINT_DATES = "./buildPISprintDates.jsp";
	
	public static final String TITLE_PROFILER = "RB Dashboard Profiler";
	public static final String TITLE_BUILD_TEAM_DASHBOARD = "Build a Team Dashboard";
	public static final String TITLE_SCHEDULE_TEAM_DASHBOARD = "Schedule a Team Dashboard";
	public static final String TITLE_BUILD_PI_SPRINT_DATES = "Build a PI and Sprint Dates";
	public static final String TITLE_TEAM_DASHBOARD_VIEW = "Team Dashboard View";
	
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
}
