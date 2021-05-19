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
	<link rel="stylesheet" href="css/userhome.css" />
	<title>Results</title>
</head>
<body>
	<div
		class="d-flex flex-column h-100 flex-md-row justify-content-between">
		<nav
			class="navbar navbar-expand-md navbar-dark flex-sm-row flex-md-column px-3 py-0 p-md-0 col-md-3 sticky-top">
			<a href="home" class="text-center h1 text-decoration-none d-none d-md-block text-white mt-3 mb-2 align-self-stretch">
				<img src="assets/logowhite.png" class="logo" />
			</a>
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
				class="collapse navbar-collapse align-items-start col-md-8"
				id="navItems">
				<ul class="nav nav-pills text-center d-flex flex-column w-100">
					<li class="nav-item pb-3 text-white"><a href="home"
						class="nav-link">Vote</a></li>
					<li class="nav-item pb-3"><a href="results"
						class="nav-link active">Results</a></li>
					<li class="nav-item pb-3"><a href="signout"
						class="nav-link sign-out">Sign Out</a></li>
				</ul>
			</div>
		</nav>
		<div class="container flex-grow-1 d-flex flex-column overflow-auto mh-100 content-frame">
			<div class="row">
				<div class="content-title col-md-12 py-3 sticky-top d-none d-md-block position-relative">
					<h4 class="text-center">
						<b></b>
					</h4>
				</div>
			</div>
			<div class="content-box position-relative row p-6 pt-3 flex-grow-1  d-block">
			
			</div>
		</div>
	</div>
	<jsp:include page="js/core-scripts.jsp" />
	<script type="text/javascript">
		const userID = "<%= user.getUserID() %>";
	</script>
	<script type="text/javascript" src="js/userresults.js"></script>
</body>
</html>