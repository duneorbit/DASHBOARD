<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ page import="com.jspeedbox.web.servlet.controller.rest.utils.RestServiceConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><%=IAction.TITLE_BUILD_TEAM_DASHBOARD%></title>
	<jsp:include page="./mediaContent.jsp"/>
	
	<script language="javascript">
	
		function clean(editing){
			if(editing.indexOf('null')!=-1){
				return "";
			}
			return editing;
		} 
	
		var editing = clean('<%=request.getParameter("dashboard")%>');
		
		function isInUsers(username, users){
			var exists = false;
			$.each($.parseJSON(JSON.stringify(users)), function(key,value){
				if(value.username==username){
					exists=true;
				}
			});
			return exists;
		}
		
		$(document).ready(function(event){
			if(editing.trim()!=''){
				$("#dashboardName").val(editing);
			}else{
				$("#dashboardName").val('');
			}
			
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
		        				var template = getDashboardButtonTemplate();
		        				template = template.replace("@developersNum@", developers);
		        				template = template.replace("@dashboardName@", dashboardName);
		        				if(editing.trim()!=''&&editing==dashboardName){
		        					template = template.replace("@color@", "panel-green");
		        					template = template.replace("@options@", "Editing");
		        					template = template.replace("@dashboard@", 'abort');
		        				}else{
		        					template = template.replace("@color@", "panel-primary");
		        					template = template.replace("@options@", "Edit");
		        					template = template.replace("@dashboard@", dashboardName);
		        				}
		        				
		        				$("#existingDashboardsHook").append(template);
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
			
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.GET_ALL_USERS%>',
				dataType: 'json',
		        async: false,
		        success: function(result) {
		        	
		        	if(result!=null){
		        		try{
		        			$.each($.parseJSON(JSON.stringify(result.users)), function(key,value){
		        				$("#user-list").append($('<option value="'+value.username+'">'+value.name+' - '+value.username+'</option>'));
		        			});
		        			$('#user-list').searchableOptionList({
						        showSelectAll: false,
						        maxHeight: 200
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
			
			if(editing.trim()!=''){
				$.ajax({
			        type: 'GET',
			        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/team/'+editing,
					dataType: 'json',
			        async: false,
			        success: function(result) {
			        	
			        	if(result!=null){
			        		try{
			        			$("#user-list > option").each(function() {
			        				if(isInUsers(this.value, result.users)){
			        					
			        					$(this).prop("selected", true);
			        				}
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
		
		function getDevelopers(){
			var users=[];
			$("#user-list > option").each(function() {
			   	if(this.selected){
			   		var user={};
			   		user.username=this.value;
			   		user.name=this.text.substring(0, this.text.indexOf(" - "));
			   		users.push(user);
			   	}
			});
			return users;
		}
		
		function validate(){
			var msg = '';
			if($("#user-list :selected").length==0){
				msg+='<li>Must add developers</li>';
			}
			if($("#dashboardName").val()==''){
				msg+='<li>Must name dashboard</li>';
			}
			if(msg.length>0){
				errorMsg(msg);
				return false;
			}
			return true;
		}
		
		function updateDashboard(){
			if(!validate()){
				return;
			}
			
			var j = serializeDB();
			
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.UPDATE_TEAM_DASHBOARD%>',
				data : JSON.stringify(j),
				dataType : 'json',
				async: false,
				success : function(dashboard) {
					if(dashboard.success==false){
						errorMsg(dashboard.msg);
					}else{
						doAction('./profiler.jsp');
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					//alert(xhr.status + " " + xhr.responseText);
				},
				done : function(e) {
					
				},
				statusCode:{
					403: function(){
						errorMsg(wrap("You are not Authorized to perform this operation! Seek Administrative Privilages!"));
					}
				}
			});
		}
		
		function createDashboard(){
			
			if(!validate()){
				return;
			}
			var j = serializeDB();

			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : 'http://localhost:8080/RB-Dashboard-Profiler/rest/<%=RestServiceConstants.CREATE_TEAM_DASHBOARD%>',
				data : JSON.stringify(j),
				dataType : 'json',
				async: false,
				success : function(dashboard) {
					if(dashboard.success==false){
						errorMsg(dashboard.msg);
					}else{
						doAction('./profiler.jsp');
					}
				},
				error : function(e) {
					alert(jqXHR.status + " " + jqXHR.responseText);
				},
				done : function(e) {
					
				}
			});
			
		}
		
		function deleteDashboard(){
			$.ajax({
		        type: 'GET',
		        url:  'http://localhost:8080/RB-Dashboard-Profiler/rest/dashboards/team/delete/'+editing,
				dataType: 'json',
		        async: false,
		        success: function(dashboard) {
		        	
		        	if(dashboard!=null){
		        		try{
		        			if(dashboard.success=='true'){
		        				doAction('./profiler.jsp');
		        			}else{
		        				errorMsg(wrap(dashboard.msg));
		        			}
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
	</script>
	
</head>
<body>
	<div id="container">
		<jsp:include page="./errorContainer.jsp"/>
		<div class="row">
			<div class="col-lg-12 jumbotron">
				<h2>&nbsp;<span class="glyphicon glyphicon-cog"></span> Team Dashboard Builder</h2>
				<p>
					&nbsp;<span class="glyphicon glyphicon-user"></span> Add team members to the dashboard, name it and save!
				</p>
			</div>
		</div>
		
		<jsp:include page="./navigation.jsp"/>
		
		<div class="row">
			<div class="col-lg-12">
				<div class="page-header">
					<h2>&nbsp;Existing Team Dashboard</h2>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Modify Existing boards</h4>
								<p>Add remove developers</p>
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
		
		<div class="row">
			<div class="col-lg-12">
				<div class="page-header">
					<h2>&nbsp;Create a New Team Dashboard</h2>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Dashboard Properties</h4>
								<p>Team Name for Dashboard.<br/>
									All Dashboards when created, will run nightly so as to facilitate comparisons.
								</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label for="dashboardName">Dashboard Name</label>
							<div class="input-group">
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-pencil"></span>
									<!-- <i class="glyphicon glyphicon-pencil"></i> -->
								</div>
								<input name="dashboardName" type="text" class="form-control" id="dashboardName" placeholder="Dashboard Name"/>
							</div>
						</div>
					</div>
				</div>	
			</div>
			<div class="col-lg-6">
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="card sb-card">
							<div class="card-block">
								<h4>Developers</h4>
								<p>Add the developers of this team so they will be included for dashboard metrics collection</p>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<div class="input-group">
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-search"></span>
									<!-- <i class="glyphicon glyphicon-pencil"></i> -->
								</div>
								<select class="selectpicker" id="user-list" name="character" multiple="multiple">
								</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<script language="javascript">
							if(editing.trim()!=''){
								document.write('<button type="Submit" class="btn btn-primary " name="updateDashboard" id="updateDashboard" onclick="javascript:updateDashboard();">Update '+editing+'</button>');
								document.write('&nbsp;<button type="Submit" class="btn btn-primary " name="saveDashboard" id="saveDashboard" onmouseover="javascript:primer('+editing+');" data-toggle="confirmation" data-on-confirm="deleteDashboard">Delete Dashboard '+editing+'</button>');
							}else{
								document.write('<button type="Submit" class="btn btn-primary " name="saveDashboard" id="saveDashboard" onclick="javascript:createDashboard();">Create Dashboard</button>');
							}
						</script>
						
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>