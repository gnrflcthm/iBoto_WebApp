package com.iboto.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.iboto.models.UserBean;

public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		RequestDispatcher rd; 
		if (session == null) {
			rd = request.getRequestDispatcher("home.jsp");
			rd.forward(request, response);
		} else {
			UserBean userBean = (UserBean) session.getAttribute("userBean");
			if (userBean == null) {
				rd = request.getRequestDispatcher("home.jsp");
				rd.forward(request, response);
			} else {
				rd = request.getRequestDispatcher("userhome.jsp");
				rd.forward(request, response);
			}
		}
	}

}
