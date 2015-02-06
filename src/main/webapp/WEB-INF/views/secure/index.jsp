<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>You are in secure path</h1>

<p><a href="/">Home page</a></p>
<!-- <p><a href="app/extreme">Extremely secure page</a></p>-->
<%if (request.isUserInRole("ROLE_ADMIN")) { %>
<p>You are an admin! You can therefore see the <a href="filtered">filtered secure page</a>.</p>
<% } %>
<p><form action="/logout"><button type="submit" formmethod="post">Logout</button></form></p>
</body>
</html>