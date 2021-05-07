<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.iboto.models.UserBean"%>
<%
	UserBean user = (UserBean) session.getAttribute("userBean");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<jsp:include page="css/head-components.jsp" />
	<link rel="stylesheet" type="text/css" href="css/userhome.css">
	<title>Vote</title>
</head>
<body>
	<div
		class="d-flex flex-column h-100 flex-md-row justify-content-between">
		<nav
			class="navbar navbar-expand-md navbar-dark flex-sm-row flex-md-column px-3 py-0 p-md-0 col-md-3 sticky-top">
			<a href="#"
				class="text-center h1 text-decoration-none d-none d-md-block text-white mt-3 mb-4 align-self-stretch"><b>Logo</b></a>
			<h4 class="text-center text-white d-sm-block d-md-none py-3">
				<b>Vote</b>
			</h4>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navItems"
				aria-controls="navItems" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div
				class="collapse navbar-collapse align-items-start mt-md-3 col-md-8"
				id="navItems">
				<ul class="nav nav-pills text-center d-flex flex-column w-100">
					<li class="nav-item pb-3 text-white"><a href="#"
						class="nav-link active">Vote</a></li>
					<li class="nav-item pb-3"><a href="results.html"
						class="nav-link">Results</a></li>
					<li class="nav-item pb-3"><a href="home.html"
						class="nav-link sign-out">Sign Out</a></li>
				</ul>
			</div>
		</nav>
		<div class="container flex-grow-1 overflow-auto mh-100 content-box">
			<div class="row">
				<div
					class="content-title col-md-12 py-3 mb-md-3 sticky-top d-none d-md-block">
					<h4 class="text-center">
						<b>Vote</b>
					</h4>
				</div>
			</div>
			<div class="row h-75 p-6">
				<!-- <div class="col-md-12 d-flex justify-content-center h-100">
					<button class="btn btn-lg align-self-center">Start Voting</button>
				</div> 
				<div class="col-md-12 d-flex justify-content-center h-100">
					<div class="spinner-border align-self-center" role="status"></div>
				</div> -->
				<div class="col-md-12 d-flex justify-content-center h-100">
					<h4 class="align-self-center text-secondary">There are no
						ongoing elections.</h4>
				</div>
			</div>
		</div>
	</div>
</body>
</html>