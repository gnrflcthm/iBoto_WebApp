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
		<jsp:include page="navigation.jsp" />
		<div
			class="container flex-grow-1 d-flex flex-column overflow-auto mh-100 content-frame">
			<div class="row">
				<div
					class="content-title col-md-12 py-3 sticky-top d-none d-md-block position-relative">
					<h4 class="text-center">
						<b>Profile</b>
					</h4>
				</div>
			</div>
			<div
				class="content-box position-relative row p-6 pt-3 flex-grow-1  d-block">
				<div class="col-12">
					<div class="card">
						<div class="card-body">
							<div class="row">
								<h5 class="card-title col-12 col-md-2">
									<b>Voter ID:</b>
								</h5>
								<h5 class="card-text col-12 col-md-4"><%=user.getUserID()%></h5>
								<h5 class="card-title col-12 col-md-2">
									<b>Name:</b>
								</h5>
								<h5 class="card-text col-12 col-md-4">
									<%
										out.println(String.format("%s, %s", user.getLastName(), user.getFirstName()));
									%>
								</h5>
								<h5 class="card-title col-12 col-md-2">
									<b>Address:</b>
								</h5>
								<h5 class="card-text col-12 col-md-4"><%=user.getCityAddress().getProperName()%></h5>
								<h5 class="card-title col-12 col-md-2">
									<b>District:</b>
								</h5>
								<h5 class="card-text col-12 col-md-4"><%=user.getDistrict()%></h5>
							</div>
							<button class="btn mt-2 d-block col-12 col-md-3" id="updatePass" data-target="#chPsFr"
								aria-expanded="false">Update Password</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="js/core-scripts.jsp" />
	<script type="text/javascript">
		const userID = "<%=user.getUserID()%>";
	</script>
	<script type="text/javascript" src="js/userprofile.js"></script>
</body>
</html>