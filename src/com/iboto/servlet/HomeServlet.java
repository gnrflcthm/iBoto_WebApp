package com.iboto.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.iboto.models.UserBean;

/**
 * HomeServlet is designed to handle attempted URL rewriting to restricted parts of the web app.
 */
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HomeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		RequestDispatcher rd; 
		// Checks for session
		if (session == null) {
			// If session does not exist, return to home page.
			rd = request.getRequestDispatcher("home.jsp");
			rd.forward(request, response);
		} else {
			// If session exists,
			if (session.getAttribute("userBean") == null) {
				// If userBean attribute is null (meaning there is no logged in user), return to home page.
				rd = request.getRequestDispatcher("home.jsp");
				rd.forward(request, response);
			} else {
				// If userBean attribute is not null (there is a user currently logged in), return to user home page.
				rd = request.getRequestDispatcher("userhome.jsp");
				rd.forward(request, response);
			}
			
		}
	}

}
