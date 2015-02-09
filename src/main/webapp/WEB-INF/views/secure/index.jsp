<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>You are in secure path</h1>

<p><a href="/">Home page</a></p>
<!-- <p><a href="app/extreme">Extremely secure page</a></p>-->
<sec:authorize access="isAuthenticated()">
<p>You are an admin! You can therefore see the <a href="filtered">filtered secure page</a>.</p>
</sec:authorize>
<p><form:form action="/logout"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button type="submit" formmethod="post">Logout</button></form:form></p>
</body>
</html>