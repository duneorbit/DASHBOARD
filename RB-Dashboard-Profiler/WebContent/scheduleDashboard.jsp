<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<%
		String dashboard = request.getParameter(IAction.PARAM_DASHBOARD);
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%=IAction.TITLE_SCHEDULE_TEAM_DASHBOARD%></title>
	<jsp:include page="./mediaContent.jsp"/>
	
	<script language="javascript">
		var selectedAdditionalSchedule = null;
		var pattern = null;
		
		$(document).ready(function(event){
			$("#selectedAdditionalSchedule").on('click', 'li a', function(){
				selectedAdditionalSchedule = $(this);
			  	$(".additionalScheduleButton:first-child").text(selectedAdditionalSchedule.text());
			    $(".additionalScheduleButton:first-child").val(selectedAdditionalSchedule.text());
		   	});
		});
	
		function validate(){
			var msg = ''
			if(pattern==null){
				msg+=wrap('Please select an additonal scheduling pattern');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function additionalSchedule(h){
			return;
		}
		
		function createAdditionalSchedule(){
			if(!validate()){
				return false;
			}
			var additionalSchedule = {}
			additionalSchedule.pattern=pattern;
			additionalSchedule.name=dashboard;
			
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.CREATE_ADDITIONAL_SCHEDULE%>',
				data : JSON.stringify(additionalSchedule),
				dataType : 'json',
				async: false,
				success : function(additionalScheduleResponse) {
					if(additionalScheduleResponse.success==false){
						errorMsg(wrap(additionalScheduleResponse.msg));
					}else{
						doAction('./profiler.jsp');
					}
				},
				error : function(e) {
					errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
				},
				done : function(e) {
					
				}
			});
		}
		
	</script>
	
</head>
<body>
	<div id="container">
		<jsp:include page="./errorContainer.jsp"/>
		<div class="row">
			<div class="col-lg-12 jumbotron">
				<h2>&nbsp;&nbsp;<span class="glyphicon glyphicon-cog"></span> Team Dashboard Scheduler</h2>
				<p>
					&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span> Each dashboard when created is automatically scheduled to run at midnight tye data minig process.
					Click start to manually run the datamining process if you need updated data now!
				</p>
			</div>
		</div>
		
		<div class="row">
			<div class="panel panel-default">
				<div class="panel-body">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="./profiler.jsp">Team dashboards</a></li>
					</ol>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-7">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Extend the scheduler to run more often. By default each dashboard builds nightly at midnight.</h4>
								<p>You may want data updated throughout the day for dashboard comparisons, large screen metrics displays etc.</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<div class="input-group">
								<label for="additionalScheduleButton" class="control-label">Additional Scheduling:</label>
								<div class="dropdown">
									<button class="btn btn-primary dropdown-toggle additionalScheduleButton" type="button"
										data-toggle="dropdown" id="additionalScheduleButton">
										Extended Schedule Options <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" id="selectedAdditionalSchedule">
									<li><a href="javascript:additionalSchedule('m5');">Every 5 Minutes</a></li>
										<li><a href="javascript:additionalSchedule('h1');">Hourly</a></li>
										<li><a href="javascript:additionalSchedule('h2');">Every 2 Hours</a></li>
										<li><a href="javascript:additionalSchedule('h4');">Every 4 Hours</a></li>
									</ul>
								</div>
								<br/>
								<button type="Submit" class="btn btn-primary " name="createAdditionalSchedule" id="createAdditionalSchedule" onclick="javascript:createAdditionalSchedule();">
									<span class="glyphicon glyphicon-time"></span>
									Add Additonal Schedule
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-5">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Manually run the datamining process here</h4>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<button type="Submit" class="btn btn-primary " name="startDataMiner" id="startDataMiner" onclick="javascript:doAction('./invokeDashboardBuild.jsp&dashboard=<%=dashboard%>');">
							<span class="glyphicon glyphicon-tasks"></span>
							Run Manualy Now
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>