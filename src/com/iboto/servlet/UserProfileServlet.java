package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.iboto.database.IBotoDbUtils;
import com.iboto.models.UserBean;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IBotoDbUtils db;
	private Gson gson;
	
	// Initializes necessary classes
	@Override
	public void init() throws ServletException {
		db = new IBotoDbUtils(this.getServletContext());
		gson = new Gson();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UserBean user = (UserBean) session.getAttribute("userBean");
		if (user == null) {
			response.sendRedirect("home");
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("userprofile.jsp");
			rd.forward(request, response);
		}
	}
	
	// Handles the request and calls the method needed
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		HttpSession session = request.getSession(false);
		UserBean user = (UserBean) session.getAttribute("userBean");
		switch(service) {
			case "password":
				updatePassword(request, response);
				break;
		}
	}
	
	// Updates the users password
	private void updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserBean user = (UserBean) request.getSession(false).getAttribute("userBean");
		String opw = request.getParameter("opw");
		String npw = request.getParameter("npw");
		String rnpw = request.getParameter("rnpw");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		boolean matches = db.validateUserLogin(user.getUserID(), opw);
		boolean isValid = validatePassword(npw, rnpw);
		if (isValid && matches) {
			db.updatePassword(user.getUserID(), npw);
			out.println("{\"status\": \"success\"}");
			out.flush();
		} else {
			out.println("{\"status\": \"error\"}");
			out.flush();
		}
	}
	
	// Checks if the entered password matches the repeat password and if it meets the minimum criteria
	private boolean validatePassword(String p1, String p2) {
		int ucase = countUppercase(p1);
		return (p1.length() > 6 && p1.equals(p2) && ucase > 0);
	}
	
	// Counts the number of uppercase characters
	private int countUppercase(String text) {
		char[] temp = text.toCharArray();
		int upperCount = 0;
		for (char x : temp) {
			upperCount += (Character.isUpperCase(x)) ? 1 : 0;
		}
		return upperCount;
	}

}
