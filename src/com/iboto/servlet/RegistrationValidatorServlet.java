package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iboto.constants.City;

/**
 * Servlet implementation class RegistrationValidatorServlet
 */
@WebServlet({ "/RegistrationValidatorServlet", "/regvalidate.jsp" })
public class RegistrationValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationValidatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int segment = Integer.valueOf(request.getParameter("validate"));
		switch(segment) {
			case 0:
				String city = request.getParameter("city");
				int districtCount = City.valueOf(city).getDistrictCount();
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				if (validateName(request)) {
					out.println(String.format("{\"valid\": true, \"district_count\": %d}", districtCount));
					out.flush();
				} else {
					out.println("{\"valid\": false}");
					out.flush();
				}
		}
		
	}
	
	private boolean validateName(HttpServletRequest req) {
		String fName = req.getParameter("fname").strip();
		String mName = req.getParameter("mname").strip();
		String lName = req.getParameter("lname").strip();
		return fName.length() > 0 && mName.length() > 0 && lName.length() > 0;
	}

}
