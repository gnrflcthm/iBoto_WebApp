<%@page import="java.util.Arrays"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.iboto.constants.City, java.util.Arrays, java.util.Comparator" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<jsp:include page="css/head-components.jsp" />
	<link rel="stylesheet" href="css/index.css" />
	<title>iBoto: Registration</title>
</head>
<body>
	<div class="container mt-3 px-4 px-md-0">
        <div class="row px-0 px-md-3 justify-content-center">
            <div class="col-md-2"></div>
            <div class="col-md-8 align-self-center">
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="card-title mb-2">
                            <h3>Logo</h3>
                        </div>
                        <form action="${pageContext.request.contextPath}/register" method="POST" id="regForm" accept-charset="utf-8">
                            <div class="row">
                                <div class="col">
                                    <div class="card-title mb-2">
                                        Select Your City
                                    </div>
                                    <select name="city" id="city-select" class="form-control">
                                        <option disabled selected hidden></option>
                                        <% 
	                                        City[] cities = City.values();
	                                		Arrays.sort(cities, new Comparator<City>() {
	                                			@Override
	                                			public int compare(City c1, City c2) {
	                                				return c1.getProperName().compareTo(c2.getProperName());
	                                			}
	                                		});
                                        	for (City city : cities) { 
                                        		out.println(String.format("<option value='%s'>%s</option>\n", city.name(), city.getProperName()));
                                        	}
                                        %>
                                        
                                    </select>
                                </div>
                                <div class="col name">
                                    <div class="card-title mb-2">Enter Your Name</div>
                                    <input type="text" name="fname" id="fname" class="name-field form-control mb-3" placeholder="First Name" autocomplete="off" required />
                                    <input type="text" name="mname" id="mname" class="name-field form-control mb-3" placeholder="Middle Name" autocomplete="off" required />
                                    <input type="text" name="lname" id="lname" class="name-field form-control mb-3" placeholder="Last Name" autocomplete="off" required />
                                    <button type="button" class="btn w-100" id="validate">Validate</button>
                                </div>
                            </div>
                            <div class="row complete-registration">
                                <div class="col-12 col-md-6 form-group">
                                    <div class="card-title my-3">Enter Your Birthday</div>
                                    <input type="date" name="birthday" id="bday" class="bday form-control" placeholder="Birthday" autocomplete="off" />
                                    <div class="card-title my-3">Select Your District</div>
                                    <select name="district" id="district-select" class="form-control">
                                    </select>
                                </div>
                                <div class=" col-12 col-md-6 form-group">
                                    <div class="card-title my-3">Enter Your Password</div>
                                    <input type="password" name="password" id="pwd" class="pwd form-control my-3" placeholder="Password" autocomplete="off" />
                                    <input type="password" name="password" id="cnpwd" class="pwd form-control my-3" placeholder="Confirm Password" autocomplete="off" />
                                </div>
                            </div>
                            <div class="row complete-registration">
                                <div class="col">
                                    <div class="card-title my-3">Enter Your Email(Optional)</div>
                                    <input type="text" name="email" id="email" class="form-control" placeholder="Email" autocomplete="off" />
                                </div>
                                <div class="col">
                                    <div class="card-title my-3">Enter Your Phone Number(Optional)</div>
                                    <input type="text" name="phoneNum" id="phoneNum" class="form-control my-3" placeholder="Phone Number" autocomplete="off" />
                                </div>
                            </div>
                            <button type="button" class="btn w-100 complete-registration" id="register">Sign Up</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-md-2"></div>
        </div>
	<jsp:include page="js/core-scripts.jsp" />
	<script src="js/reg.js"></script>
</body>
</html>