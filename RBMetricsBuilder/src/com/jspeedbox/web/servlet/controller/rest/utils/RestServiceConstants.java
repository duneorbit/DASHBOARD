package com.jspeedbox.web.servlet.controller.rest.utils;

public class RestServiceConstants {
	
	//schedule
	public static final String CREATE_ADDITIONAL_SCHEDULE = "dashboards/team/schedule/additional";
	public static final String UPDATE_ADDITIONAL_SCHEDULE = "dashboards/team/schedule/additional/update";
	public static final String GET_ALL_SCHEDULED_JOBS = "dashboards/schedule/jobs/all";
	
	//users
	public static final String GET_ALL_USERS = "users/all";
	
	//dashboard
	public static final String CREATE_TEAM_DASHBOARD = "dashboards/team/create";
	public static final String UPDATE_TEAM_DASHBOARD = "dashboards/team/update";
	public static final String DELETE_TEAM_DASHBOARD = "dashboards/team/delete/{dashboard}";
	public static final String ALL_TEAM_DASHBOARD = "dashboards/team/all";
	public static final String GET_TEAM_DASHBOARD = "dashboards/team/{dashboard}";
	public static final String START_TEAM_DASHBOARD_DATAMINE = "dashboards/datamine";
	public static final String GET_TEAM_DASHBOARD_DATAMINE = "dashboards/datamine/{dashboard}";
	public static final String GET_TEAM_DASHBOARD_COMPILE = "dashboards/compiler/{dashboard}";
	public static final String GET_RECOMPILE_TEAM_DASHBOARD = "dashboards/recompile/{dashboard}";
	
	//PI sprint dates
	public static final String CREATE_PI_SPRINT_CONFIG = "dashboards/pi/create";
	public static final String GET_PI_SPRINT_CONFIG = "dashboards/pi";
	
	//dashboard presentation data
	public static final String GET_PRESENTATION_GRAPHS_DATA = "dashboards/presentation/graphs/data/{team}";
	public static final String GET_INDEXING_DATA = "dashboards/presentation/indexing/data/{team}";
	public static final String GET_METRICS_SUMMARY_DATA = "dashboards/presentation/tables/metrics/summary/data/{team}";
	public static final String GET_METRICS_REVIEWERS_DATA = "dashboards/presentation/tables/metrics/reviewers/data/{team}";
	public static final String GET_METRICS_SUMMARY_BUTTONS = "dashboards/presentation/buttons/picomments/{team}";
	
	//dahsboard interactive services
	//get developers table showing reviews per spring
	public static final String GET_DEVELOPERS_REVIEW_TABLE = "dashboards/interactive/developers/reviewstable/{team}/{filename}";

}
