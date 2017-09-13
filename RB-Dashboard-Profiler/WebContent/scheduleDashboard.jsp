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
			  	$(".additionalScheduleButton:first-child").text("Extend Dashboard " + $("#dashboardToSchedule").text() + " to also run " + selectedAdditionalSchedule.text());
			    $(".additionalScheduleButton:first-child").val(selectedAdditionalSchedule.text());
			    pattern=selectedAdditionalSchedule.text();
		   	});
			
			var index = 0;
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.GET_ALL_SCHEDULED_JOBS%>',
				dataType: 'json',
		        async: false,
		        cache: false,
		        success: function(scheduledJobsResponse) {
		        	if(scheduledJobsResponse!=null){
		        		$.each($.parseJSON(JSON.stringify(scheduledJobsResponse.jobs)), function(key,value){
		        			index++;
		        			var template = getST();
		        			template = template.replace("@INDEX@",index);
		        			template = template.replace("@NAME@",value.name);
		        			template = template.replace("@GROUP@",value.group);
			        		template = template.replace("@SCHEDULED@", value.scheduleName);
			        		template = template.replace("@LASTRUN@", value.lastRunTimeFormatted);
			        		var lr = "Not Run Yet";
			        		if(value.lastRunBy!=null&&value.lastRunBy!=''){
			        			lr = value.lastRunBy;
			        		}
			        		template = template.replace("@LASTRUNBY@", lr);
			        		$("#scheduleStatusTable").append(template);
			        		
			        		if(value.additionalSchedule!=null){
			        			index++;
			        			template = getST();
			        			template = template.replace("@INDEX@",index);
			        			template = template.replace("@NAME@",value.additionalSchedule.scheduleName);
			        			template = template.replace("@GROUP@",value.group);
				        		template = template.replace("@SCHEDULED@", value.additionalSchedule.pattern);
				        		template = template.replace("@LASTRUN@", value.additionalSchedule.lastRunTime);
				        		template = template.replace("@LASTRUNBY@", value.additionalSchedule.lastRunBy);
				        		
				        		var optionTemplate = getOptionTemplate();
				        		
				        		optionTemplate = optionTemplate.replace('@DBDelete@', value.additionalSchedule.scheduleName);
				        		optionTemplate = optionTemplate.replace('@DBSuspend@', value.additionalSchedule.scheduleName);
				        		template = template.replace('@OPTIONS@',optionTemplate);
				        		
				        		$("#scheduleStatusTable").append(template);
				        		
				        		if(value.group=='<%=dashboard%>'){
				        			$(".additionalScheduleButton:first-child").text("Extend Dashboard " + $("#dashboardToSchedule").text() + " to also run " + value.additionalSchedule.pattern);
				    			    $(".additionalScheduleButton:first-child").val(value.additionalSchedule.pattern);
				    			    pattern=value.additionalSchedule.pattern;
				    			    $("#createAdditionalSchedule").attr("onclick","updateSchedule()");
				    			    $("#createAdditionalSchedule").html('<span class="glyphicon glyphicon-time"></span> Update Additional Schedule');
				        		}
			        		}
		        		});
		        	}
		        	
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
		        }
		    });
			
		});
		
		function updateSchedule(){
			if(!validate()){
				return false;
			}
			
			var additionalSchedule = getAdditonalSchedule();
			
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.UPDATE_ADDITIONAL_SCHEDULE%>',
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
					alert();
					alert(jqXHR.status + " " + jqXHR.responseText);
				},
				done : function(e) {
					
				}
			});
		}
	
		function validate(){
			var msg = ''
			if(pattern==null||pattern==''){
				msg+=wrap('Please select an additonal scheduling pattern');
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		var patternKey=null;
		function patternKey(h){
			patternKey=h;
		}
		
		function getAdditonalSchedule(){
			var additionalSchedule = {}
			additionalSchedule.pattern=pattern;
			additionalSchedule.scheduleName=$("#dashboardToSchedule").text();
			additionalSchedule.dashboard=$("#dashboardToSchedule").text();
			additionalSchedule.lastRunTime=0;
			additionalSchedule.success=false;
			additionalSchedule.msg="";
			additionalSchedule.scheduleName="";
			additionalSchedule.lastRunBy="";
			additionalSchedule.patternKey=patternKey;
			return additionalSchedule;
		}
		
		function createAdditionalSchedule(){
			if(!validate()){
				return false;
			}
			
			var additionalSchedule = getAdditonalSchedule();
			
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
					alert();
					alert(jqXHR.status + " " + jqXHR.responseText);
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
					&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span> Each dashboard when created is automatically scheduled to run at midnight by the data minig process.
					<br/>
					&nbsp;&nbsp;&nbsp;Click "Run Manually Now" to manually run the datamining process if you need updated data now!
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
								<label for="additionalScheduleButton" class="control-label">Additional Scheduling Options for Dashboard <strong style="color:red;" id="dashboardToSchedule"><%=dashboard%></strong></label>
								<div class="dropdown">
									<button class="btn btn-primary dropdown-toggle additionalScheduleButton" type="button"
										data-toggle="dropdown" id="additionalScheduleButton">
										Extended Schedule Options <span class="caret"></span>
									</button>
									<ul class="dropdown-menu" id="selectedAdditionalSchedule">
									<li><a href="javascript:additionalSchedule('m5');">Every 5 Minutes</a></li>
										<li><a href="javascript:patternKey('h1');">Hourly</a></li>
										<li><a href="javascript:patternKey('h2');">Every 2 Hours</a></li>
										<li><a href="javascript:patternKey('h4');">Every 4 Hours</a></li>
									</ul>
								</div>
								<br/>
								<button type="Submit" class="btn btn-primary " name="createAdditionalSchedule" id="createAdditionalSchedule" onclick="javascript:createAdditionalSchedule();">
									<span class="glyphicon glyphicon-time"></span>
									Save Additional Schedule
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
		<div class="row">
			<div class="col-lg-7">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Status of all Scheduled Jobs.</h4>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="table-responsive" style="overflow-y: hidden; overflow-x: hidden;">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>#</th>
										<th>Name</th>
										<th>Group</th>
										<th>Scheduled</th>
										<th>Last Run</th>
										<th>Last Run By</th>
										<th>Action</th>
									</tr>
								</thead>
								<tbody id="scheduleStatusTable">
									
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-5">
			</div>
		</div>
	</div>
</body>
</html>