<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%
	String header = response.getHeader("invalidLogin");
	boolean invalidCredentials = true;
	if (header != null) {
		try {
			invalidCredentials = Boolean.getBoolean(header);
		} catch (Exception e) {
			out.println(e);
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<jsp:include page="css/head-components.jsp" />
	<link rel="stylesheet" href="css/index.css" />
	<title>iBoto</title>
</head>
<body>
	<div class="container mt-3 px-4 px-md-0">
        <div class="row my-4">
        	<div class="col-12 col-md-2">
	            <img class="logo" src="assets/logo.png" />
        	</div>
        </div>
        <div class="row px-0 px-md-3">
            <div class="col-12 col-md-6 my-2 my-md-0">
                <div class="card">
                    <div class="card-body">
                        <h4 class="card-title"><b>Are You A New User?</b></h4>
                        <p class="card-text text-dark">
                            Sign Up now to vote anytime and anywhere you like.
                        </p>
                        <a href="register.jsp" class="btn">Sign Up</a>
                    </div>
                </div>
            </div>
            <div class="col-md-2"></div>
            <div class="col-12 col-md-4 my-4 my-md-0">
                <div class="card">
                    <div class="card-body">
                        <h4 class="card-title"><b>Already Registered?</b></h4>
                        <p class="card-text text-dark">
                            Sign In Here
                        </p>
                        <form action="${pageContext.request.contextPath}/login" method="POST">
                        	<%
                        		if (!invalidCredentials) {
                        	%>
                        			<small class="text-danger">Invalid Login Credentials</small>
                        	<%
                        		}
                        	%>
                            <div class="form-group my-3">
                                <input type="text" name="id" id="" class="form-control" placeholder="Email or VoterID" autocomplete="off">
                            </div>
                            <div class="form-group my-3">
                                <input type="password" name="password" id="" class="form-control" placeholder="Password">
                            </div>
                            <input type="submit" class="btn w-100" value="Sign In">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="js/core-scripts.jsp" />
</body>
</html>