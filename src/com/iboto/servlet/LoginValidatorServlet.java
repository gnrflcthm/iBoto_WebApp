package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iboto.database.IBotoDbUtils;;
/**
 * Servlet implementation class LoginValidatorServlet
 */
@WebServlet("/login")
public class LoginValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IBotoDbUtils db;
	
	public void init() {
		db = IBotoDbUtils.getInstance(this.getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("id");
		String pass = request.getParameter("password");
		
		RequestDispatcher rd;

		boolean login = db.validateUserLogin(user, pass);
		if (login) {
			response.getWriter().println("Login Successfully");
		} else {
			response.addHeader("invalidLogin", "true");
			rd = request.getRequestDispatcher("home.jsp");
			rd.forward(request, response);
		}
		
	}

}
