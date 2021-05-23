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
						<b></b>
					</h4>
				</div>
			</div>
			<div
				class="content-box position-relative row p-6 pt-3 flex-grow-1  d-block">

			</div>
		</div>
	</div>
	<jsp:include page="js/core-scripts.jsp" />
	<script type="text/javascript">
		const userID = "<%=user.getUserID()%>";
	</script>
	<script type="text/javascript" src="js/userresults.js"></script>
</body>
</html>