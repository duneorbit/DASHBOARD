<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<title>Login Page</title>
<style>
.errorblock {
	color: #ff0000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}

body {
	font-family: Tahoma;
	font-size: 12px
}
td {
	font-family: Tahoma;
	font-size: 12px
}
h3 {
	font-family: Tahoma;
	font-size: 12px
}
h2 {
	font-family: Tahoma;
	font-size: 10px
}
input {
	font-family: Tahoma;
	font-size: 12px	
}
</style>
</head>
<body onload='document.f.j_username.focus();'>
	<h3>Order WareHouse Dashboard</h3>
	<h2>Please insert your credentials.</h2>
	<c:if test="${not empty param.error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>

	<form name='f' action="<c:url value='j_spring_security_check' />"
		method='POST'>

		<table>
			<tr>
				<td>User:</td>
				<td><input type='text' name='j_username' value=''></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' name='j_password' /></td>
			</tr>
			<tr>
				<td colspan='2'><input name="submit" type="submit" value="Send" /></td>
			</tr>
		</table>

	</form>
</body>
</html>