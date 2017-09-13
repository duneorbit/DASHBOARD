<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="com.jspeedbox.web.servlet.action.IAction"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=IAction.TITLE_LOGIN%></title>
	<jsp:include page="./mediaContent.jsp"/>
	<script language="javascript">
		function secureLogin(){
			$("#secureLogin").submit();
		}
	</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-4 col-md-offset-4">
				<div class="login-panel panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">Please Sign In</h3>
						<c:if test="${not empty param.error}">
							<div class="error">
								Your login attempt was not successful, try again.<br /> Caused :
								${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
							</div>
						</c:if>
					</div>
					<div class="panel-body">
						<form role="form" name="login" id="secureLogin" action="<c:url value='/j_spring_security_check'/>" method="POST">
							<fieldset>
								<div class="form-group">
									<input type="text" class="form-control"  name="j_username" autofocus="" />
								</div>
								<div class="form-group">
									<input class="form-control" name="j_password" type="password" value="" />
								</div>
								<div class="checkbox">
									<label> <input name="remember" type="checkbox"
										value="Remember Me" /> Remember Me
									</label>
								</div>
								<a href="#" class="btn btn-lg btn-success btn-block" onclick="javascript:secureLogin();">Login</a>
							</fieldset>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>