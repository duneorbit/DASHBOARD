<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.validator.ConfigValidator"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%
		ConfigValidator.validateConfigs(request, response);
		Boolean userXml = new Boolean(true);
		Boolean dashBoardsSummaryXml = new Boolean(true);
		Boolean piSprintDatesConfigXml = new Boolean(true);
		if(request.getAttribute(IAction.REQUEST_ATTRIBUTE_USERS_XML)!=null){
			userXml = (Boolean)request.getAttribute(IAction.REQUEST_ATTRIBUTE_USERS_XML);
		}
		if(request.getAttribute(IAction.REQUEST_ATTRIBUTE_TEAMS_DASHBOARD_XML)!=null){
			dashBoardsSummaryXml = (Boolean)request.getAttribute(IAction.REQUEST_ATTRIBUTE_TEAMS_DASHBOARD_XML);
		}
		if(request.getAttribute(IAction.REQUEST_ATTRIBUTE_PI_SPRINT_DATES)!=null){
			piSprintDatesConfigXml = (Boolean)request.getAttribute(IAction.REQUEST_ATTRIBUTE_PI_SPRINT_DATES);
		}
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%=IAction.TITLE_PROFILER%></title>
	
	<jsp:include page="./mediaContent.jsp"/>
    
    <script language="javascript">
    	var doLoadDashboards = true;
    	
		
		
		$(document).ready(function(event){
			if(doLoadDashboards){
				$.ajax({
			        type: 'GET',
			        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.ALL_TEAM_DASHBOARD%>',
					dataType: 'json',
			        async: false,
			        success: function(result) {
			        	if(result!=null){
			        		try{
			        			$.each($.parseJSON(JSON.stringify(result.dashboards)), function(key,value){
			        				var dashboardName = value.dashboardName;
			        				var developers = value.users.length
			        				var template = getDashboardButtonTemplateOptions();
			        				template = template.replace("@dashboard@", dashboardName);
			        				template = template.replace("@developersNum@", developers);
			        				template = template.replace("@dashboardName@", dashboardName);
			        				template = template.replace("@color@", "panel-primary");
			        				$("#existingDashboardsHook").append(template);
			        			});
			        			$('[data-toggle=dashboard-action]').confirmation({
			        			    buttons: [
			        			      {
			        			        label: 'Recompile Dashboard',
			        			        class: 'btn btn-xs btn-success',
			        			        icon: 'glyphicon glyphicon-ok',
			        			        onClick: function(){
			        			        	
			        			        	$.ajax({
										        type: 'GET',
										        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/recompile/'+primedDashboard,
												dataType: 'json',
										        async: true,
										        cache: false,
										        success: function(response) {
										        	
										        	if(response!=null){
										        		if(response.success==true){
										        			successMsg(wrap("Successfully recompiled dashboard"));
										        		}else{
										        			errorMsg(wrap(response.msg));
										        		}
										        	}else{
										        		errorMsg(wrap("Failed to recompile dashboard"));
										        	}
										        	
										        },
										        error: function(jqXHR, textStatus, errorThrown) {
										        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
										        }});
			        			        }
			        			      },
			        			      {
			        			        label: 'Edit Dashboard',
			        			        class: 'btn btn-xs btn-danger',
			        			        icon: 'glyphicon glyphicon-edit',
										onClick: function(){
											doAction('./buildTeamDashboard.jsp&dashboard='+primedDashboard);
			        			        }
			        			      },
			        			      {
			        			        label: 'View Dashboard',
			        			        class: 'btn btn-xs btn-warning',
			        			        icon: 'glyphicon glyphicon-search',
										onClick: function(){
											doAction('./viewDashboard.jsp&dashboard='+primedDashboard);
			        			        }
			        			      },
			        			      {
			        			        label: 'Schedule Dashboard',
			        			        class: 'btn btn-xs btn-default',
			        			        icon: 'glyphicon glyphicon-time',
										onClick: function(){
											doAction('./scheduleDashboard.jsp&dashboard='+primedDashboard);
			        			        }
			        			      },
			        			      {
			        			        label: 'Cancel',
			        			        class: 'btn btn-xs btn-default',
			        			        icon: 'glyphicon glyphicon-remove-sign',
										onClick: function(){
											primedDashboard = null;
			        			        }
			        			      }
			        			    ]
			        			  });
			        			
			        		}catch(e){
			        			errorMsg(wrap(e));
			        		}
			        	}
			        },
			        error: function(jqXHR, textStatus, errorThrown) {
			        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
			        }
			    });
			}
		});
	</script>
</head>
<body>
	 <div id="container">
	 <jsp:include page="./errorContainer.jsp"/>
		<div class="row">
			<div class="col-lg-12 jumbotron">
				<h2>&nbsp;&nbsp;<span class="glyphicon glyphicon-cog"></span> Teams Governance Metrics Dashboard</h1>
			</div>
		</div>
		
		<jsp:include page="./navigation.jsp"/>
		
		<%if(userXml.booleanValue()==false){%>
			<script language="javascript">doLoadDashboards = false;</script>
			<div class="row">
				<div class="col-lg-12">
					There are no users data mined from review board, 
					<button type="button" class="btn btn-primary" onclick="javascript:doAction('<%=IAction.ACTION_USER_BUILD_ACTION%>');">
						Start users collection process
					</button>
				</div>
			</div>
		<%}else{
			if(dashBoardsSummaryXml.booleanValue()==false){
				//need to build at least one
				%>
					<script language="javascript">doLoadDashboards = false;</script>
					<div class="row">
						<div class="col-lg-12">
							There are no team dashboards available, at least one must be created, 
							<button type="button" class="btn btn-primary" onclick="javascript:doAction('<%=IAction.JSP_BUILD_TEAM_SAHBOARD%>');">
								Create a Team Dashboard
							</button>
						</div>
					</div>
				<%
			}else if(piSprintDatesConfigXml.booleanValue()==false){
				%>
					<script language="javascript">doLoadDashboards = false;</script>
					<div class="row">
						<div class="col-lg-12">
							There are no PI and sprint dates configured, 
							<button type="button" class="btn btn-primary" onclick="javascript:doAction('<%=IAction.JSP_CREATE_PI_SPRINT_DATES%>');">
								Create PI Sprint Dates
							</button>
						</div>
					</div>
				<%
			}else{
				%>
					<div class="row">
						<div class="col-lg-12">
							<div class="page-header">
								<h2>&nbsp;Team Dashboards</h2>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="panel panel-default">
								<div class="panel-heading">
									<div class="card sb-card">
										<div class="card-block">
											<h4>Click to view current dashboard details</h4>
											<p>View metrics on:
												<ul>
													<li>Summary Detail of Code Reviews</li>
													<li>Developer Code Reviews Summary</li>
													<li>New Files vs Existing files</li>
													<li>Developer comments shown accross each PI</li>
													<li>Comments received by team members per sprint for each PI</li>
													<li>Detailed break down analysis of review comments by reviewers</li>
													<li>Visual comment Category indexing</li>
													<li>Comment Clouds and more ...</li>
												</ul>
											</p>
										</div>
									</div>
								</div>
								<div class="panel-body">
									<div class="row" id="existingDashboardsHook">
											
									</div>
								</div>
							</div>
						</div>
					</div>
				<%
			}
		}%>
	</div>
</body>
</html>