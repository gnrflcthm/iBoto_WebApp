package com.iboto.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import com.iboto.util.PdfHelper;

/**
 * Servlet implementation class ResultsServlet
 */
@WebServlet("/results")
public class ResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private IBotoDbUtils db;
	private Gson gson;
	
    public void init() {
    	db = new IBotoDbUtils(this.getServletContext());
    	gson = new Gson();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher rd;
		if (session.getAttribute("userBean") == null) {
			response.sendRedirect("home");
		} else {
			String referenceNumber = request.getParameter("refNum");
			if (referenceNumber == null) {
				rd = request.getRequestDispatcher("userresults.jsp");
				rd.forward(request, response);				
			} else {
				downloadVoteSummary(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		switch(service) {
			case "elections":
				getElections(request, response);
				break;
			case "votedcandidates":
				getCandidates(request, response);
				break;
			case "electionpoll":
				getElectionPoll(request, response);
		}
	}
	
	private void getElections(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("uid");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.println(gson.toJson(db.getUserVotes(userID)));
		out.flush();
	}
	
	private void getCandidates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String voteID = request.getParameter("voteID");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.println(gson.toJson(db.getVotedCandidates(voteID)));
		out.flush();
	}
	
	private void downloadVoteSummary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String referenceNumber = request.getParameter("refNum");
		UserBean user = (UserBean) request.getSession().getAttribute("userBean");
		
		String filePath  = request.getServletContext().getRealPath("") + "temp\\" + referenceNumber + ".pdf";  
		PdfHelper.generateVoteSummary(filePath, db.getVoteSummary(referenceNumber), db.getVotedCandidates(referenceNumber), user);
		
		File downloadFile = new File(filePath);
		FileInputStream fis = new FileInputStream(downloadFile);
		
		String mimeType = this.getServletContext().getMimeType(filePath);
		
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", referenceNumber); 
        response.setHeader(headerKey, headerValue);
        
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytes = -1;
        while ((bytes = fis.read(buffer)) != -1) {

        	os.write(buffer, 0, bytes);
        }
        fis.close();
        os.close();
        downloadFile.delete();
	}
	
	private void getElectionPoll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String electionId = request.getParameter("electionID");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		out.println(gson.toJson(db.getElectionPoll(electionId)));
		out.flush();
	}

}
