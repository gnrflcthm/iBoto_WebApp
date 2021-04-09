<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.iboto.models.UserBean" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		UserBean user = (UserBean) session.getAttribute("userBean");
	%>
	<%= user.getUserID() %>
	<%= user.getEmail() %>
	<%= user.getPhoneNum() %>
	<%= user.getFirstName() %>
	<%= user.getLastName() %>
	<%= user.getCityAddress().getProperName() %>
	<%= user.getDistrict() %>
</body>
</html>