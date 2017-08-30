<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%=IAction.TITLE_BUILD_TEAM_DASHBOARD%></title>
	<jsp:include page="./mediaContent.jsp"/>
	
	<script language="javascript">
	
		
		var dashboardToRun = '<%=request.getParameter("dashboard")%>';
		var running = false;
		
		$(document).ready(function(event){
			
			if(dashboardToRun.trim()!=''){
				$.ajax({
			        type: 'GET',
			        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/team/'+dashboardToRun,
					dataType: 'json',
			        async: false,
			        success: function(dashboard) {
			        	
			        	if(dashboard!=null){
			        		try{
			        			$.each($.parseJSON(JSON.stringify(dashboard.users)), function(key,value){
			        				var template = getThreadProgressTemplate();
			        				template = template.replace("@developer@", value.name.substring(value.name.indexOf(0, value.name.indexOf(" - "))));
			        				template = template.replace("@thread@", value.username);
			        				$("#datamineDevelopersHook").append(template);
			        			});
			        			
			        			$("#datamineDevelopersHook").append(dmp());
			        			$("#datamineDevelopersHook").append(ctp());
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
		
		function dmp(){
			var template = '';
			template+='<div class="row">';
			template+='	<div class="col-lg-9">';
			template+='		<i class="glyphicon glyphicon-user fa-1x"></i>';
			template+='		<label>Data Mine Progress</label>';
			template+='	</div>';
			template+='</div>';
			template+='<div class="row">';
			template+='	<div class="col-lg-9">';
			template+='		<div class="progress">';
			template+='			<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="0"';
			template+='				aria-valuemin="0" aria-valuemax="100" style="width: 0%" id="dataMineStatus">';
			template+='				<span class="sr-only">0% Complete</span>';
			template+='			</div>';
			template+='		</div>';
			template+='	</div>';
			template+='</div>';
			return template;
		}
		
		function ctp(){
			var template = '';
			template+='<div class="row">';
			template+='	<div class="col-lg-9">';
			template+='		<i class="glyphicon glyphicon-user fa-1x"></i>';
			template+='		<label>Compile Progress</label>';
			template+='	</div>';
			template+='</div>';
			template+='<div class="row">';
			template+='	<div class="col-lg-9">';
			template+='		<div class="progress">';
			template+='			<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="0"';
			template+='				aria-valuemin="0" aria-valuemax="100" style="width: 0%" id="compileStatus">';
			template+='				<span class="sr-only">0% Complete</span>';
			template+='			</div>';
			template+='		</div>';
			template+='	</div>';
			template+='</div>';
			return template;
		}
		
		function startDataMiner(){
			if(!running){
				running = true;
				$("#startDataMiner").html(' <span class="glyphicon glyphicon-tasks"></span> Starting');
				var j = {};
				j.dashboard = dashboardToRun;
				j.started = false;
				$.ajax({
					type : "POST",
					contentType : "application/json",
					url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.START_TEAM_DASHBOARD_DATAMINE%>',
					data : JSON.stringify(j),
					dataType : 'json',
					async: false,
					success : function(dataMineResponse) {
						
						if(dataMineResponse.started==false){
							errorMsg(wrap(dataMineResponse.msg));
						}else{
							//start poll
							$("#startDataMiner").html(' <span class="glyphicon glyphicon-tasks"></span> Running');
							pollDataminerProcess();
						}
					},
					error : function(e) {
						errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
					},
					done : function(e) {
						
					}
				});
			}
		}
		
		function pollDataminerProcess(){
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/datamine/'+dashboardToRun,
				dataType: 'json',
		        async: false,
		        success: function(dataMineResponse) {
		        	//logger.debug('dataMineResponse['+JSON.stringify(dataMineResponse)+']');
		        	update(dataMineResponse);
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
		        }
		    });
		}
		
		function pollCompilerProcess(){
			//logger.debug('pollCompilerProcess');
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/compiler/'+dashboardToRun,
				dataType: 'json',
		        async: false,
		        success: function(compilerResponse) {
		        	//logger.debug('raw['+compilerResponse+'] parsed['+JSON.stringify(compilerResponse)+']');
		        	updateComp(compilerResponse);
		        },
		        error: function(jqXHR, textStatus, errorThrown) {
		        	errorMsg(wrap(jqXHR.status + " " + jqXHR.responseText));
		        }
		    });
		}
		
		function updateComp(compilerResponse){
			if(compilerResponse.finished==true){
				compilerResponse.pec=100;
				compilerResponse.complete=1;
				compilerResponse.all=1;
			}
			
			$("#compileStatus").css("width", compilerResponse.pec+"%");
			
			if(compilerResponse.complete==compilerResponse.all){
				successMsg(wrap("Complete!"));
				//logger.debug('compiled[done]');
				$("#startDataMiner").html(' <span class="glyphicon glyphicon-tasks"></span> Start');
				running = false;
			}else{
				setTimeout("pollCompilerProcess()", 2000);
			}
		}
		
		function update(dataMineResponse){
			try{
				var size = Object.keys(dataMineResponse.status).length;
				var completed = 0;
				
				$("#dataMineStatus").css("width", dataMineResponse.overAllPerc+"%");
				
				if(Object.keys(dataMineResponse.status).length === 0){
					return setTimeout("pollDataminerProcess()", 2000);
				}
				
				$.each($.parseJSON(JSON.stringify(dataMineResponse.status)), function(key,value){
					if(value.pec==100){
						completed++;
					}
					$("#"+value.username).css("width", value.pec+"%");
				});
				
				if(completed!=size || dataMineResponse.msg=='retry'){
					//logger.debug('completed['+completed+'] size['+size+'] dataMineResponse.msg['+dataMineResponse.msg+']');
					setTimeout("pollDataminerProcess()", 2000);
				}else{
					//logger.debug('poll compiler');
					pollCompilerProcess();
				}
			}catch(e){
				errorMsg(wrap(e));
			}
		}
		
	</script>
	
</head>
<body>
	<div id="container">
		<jsp:include page="./errorContainer.jsp"/>
		<div class="row">
			<div class="col-lg-12 jumbotron">
				<h2>&nbsp;&nbsp;<span class="glyphicon glyphicon-cog"></span> Team Dashboard Dataminer</h2>
				<p>
					&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span> Click start to kick of manual data mine process of your dashboard!
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
								<h4>Progress view for each developer</h4>
								<p>Developers are data mined in parallel for speed and efficency</p>
							</div>
						</div>
					</div>
					<div class="panel-body" id="datamineDevelopersHook">
						
					</div>
				</div>
			</div>
			<div class="col-lg-5">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Start the process here</h4>
								<p>You can leave this page once clicking start, and you will get notified on completion, as long as you say anywhere in the web app!</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<button type="Submit" class="btn btn-primary " name="startDataMiner" id="startDataMiner" onclick="javascript:startDataMiner();">
							<span class="glyphicon glyphicon-tasks"></span>
							Start
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>