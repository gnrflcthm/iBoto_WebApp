package com.iboto.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iboto.constants.City;
import com.iboto.database.IBotoDbUtils;
import com.thiam.encryption.HashUtils;
/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RegisterServlet() {
        super();
    }

    
    // Handles user registration and insertion to database, then redirects back to home page.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		IBotoDbUtils db = new IBotoDbUtils(this.getServletContext());
		String email = request.getParameter("email");
		String phoneNum = request.getParameter("phoneNum");
		String lastName = request.getParameter("lname");
		String firstName = request.getParameter("fname");
		String birthday = request.getParameter("birthday");
		City address = City.valueOf(request.getParameter("city"));
		int district = Integer.valueOf(request.getParameter("district"));
		String password = request.getParameter("password");
		
		db.addUser(email, phoneNum, lastName, firstName, birthday, address, district, password);
		response.sendRedirect("home.jsp");
	}
}
