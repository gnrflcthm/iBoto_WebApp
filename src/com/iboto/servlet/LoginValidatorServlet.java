package com.iboto.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.iboto.database.IBotoDbUtils;
import com.iboto.models.UserBean;
/**
 * Servlet implementation class LoginValidatorServlet
 */
@WebServlet("/login")
public class LoginValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IBotoDbUtils db;
	
	public void init() {
		// Creates instance of database for data retrieval and other actions.
		db = IBotoDbUtils.getInstance(this.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Any attempts on accessing this servlet through GET mehthods will redirect them to home servlet.
		resp.sendRedirect(this.getServletContext().getContextPath() + "/home");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("id");
		String pass = request.getParameter("password");
		
		RequestDispatcher rd;
		
		// Validates Login credentials
		boolean login = db.validateUserLogin(user, pass);
		if (login) {
			// If login is successful, creates user instance and session, then redirect them back to home servlet.
			UserBean userBean = db.getUserInstance(user);
			HttpSession session = request.getSession(true);
			if (userBean != null) {
				session.setAttribute("userBean", userBean);
				response.sendRedirect(this.getServletContext().getContextPath() + "/home");
			}
		} else {
			// If login failed, return to home.jsp with added invalidLogin header.
			response.addHeader("invalidLogin", "true");
			rd = request.getRequestDispatcher("home.jsp");
			rd.forward(request, response);
		}
		
	}

}
