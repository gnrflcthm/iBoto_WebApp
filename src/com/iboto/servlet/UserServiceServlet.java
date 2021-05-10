package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.iboto.database.IBotoDbUtils;

@WebServlet("/service")
public class UserServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IBotoDbUtils db;
	
	public UserServiceServlet() {
		db = new IBotoDbUtils(this.getServletContext());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		switch(service) {
			case "election":
				
		}
	}

}
