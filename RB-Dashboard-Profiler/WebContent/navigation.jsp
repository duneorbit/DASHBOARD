<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%
		String dashboard = request.getParameter(IAction.PARAM_DASHBOARD);
	%>
<div class="row">
	<div class="col-lg-12">
		<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="#" onclick="javascript:doAction('<%=IAction.JSP_PROFILER%>');">Team Dashboards</a></li>
					<%if(dashboard!=null){ %>
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">Dashboard <%=dashboard%> Options<span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="#">Recompile</a></li>
								<li><a href="#">Edit</a></li>
								<li><a href="#">Refresh</a></li>
								<li role="separator" class="divider"></li>
								<li class="dropdown-header">Schedule</li>
								<li role="separator" class="divider"></li>
								<li class="dropdown-header">Export</li>
							</ul>
						</li>
					<%}%>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#" onclick="javascript:doAction('<%=IAction.ACTION_USER_BUILD_ACTION%>');">Update Users <span class="sr-only">(current)</span></a></li>
					<li><a href="#" onclick="javascript:doAction('<%=IAction.JSP_BUILD_TEAM_SAHBOARD%>');">Create Team Dashboard</a></li>
					<li><a href="#" onclick="javascript:doAction('<%=IAction.JSP_CREATE_PI_SPRINT_DATES%>');">Modify/Add Sprint Dates</a></li>
					<li><a href='<c:url value="/j_spring_security_logout" />'>Logout</a></li>
				</ul>
			</div>
		</div>
		</nav>
	</div>
</div>