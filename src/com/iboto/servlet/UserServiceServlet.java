package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.iboto.database.IBotoDbUtils;
import com.iboto.models.UserBean;

@WebServlet("/service")
public class UserServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private IBotoDbUtils db;
	private Gson gson;
	
	// Initializes needed classes
	public void init() {
		this.db = new IBotoDbUtils(this.getServletContext());
		this.gson = new Gson();
	}
	
	// Handles the request and calls the specific method needed
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		switch(service) {
			case "election":
				loadElections(request, response);
				break;
			case "voteCheck":
				hasVoted(request, response);
				break;
			case "candidates":
				loadCandidates(request, response);
				break;
			case "vote":
				submitVote(request, response);
				break;
		}
	}
	
	// Returns Available elections
	private void loadElections(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		UserBean user = db.getUserInstance(request.getParameter("uid"));
		out.println(gson.toJson(db.getElections(user.getCityAddress())));
		out.flush();
	}
	
	// Checks if the users has already voted on an election
	private void hasVoted(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String userID = request.getParameter("uid");
		String electionID = request.getParameter("electionID");
		Date dateVoted = db.getVoted(userID, electionID);
		if (dateVoted == null) {
			out.println(String.format("{\"voted\": \"%s\"}", false));			
		} else {
			out.println(String.format("{\"voted\": \"%s\", \"dateVoted\": \"%s\"}", true, dateVoted));
		}
		out.flush();
	}
	
	// Loads the candidates for an election so that the user can proceed to voting
	private void loadCandidates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		String userID = request.getParameter("uid");
		String electionID = request.getParameter("electionID");
		UserBean user = db.getUserInstance(userID);
		int district = user.getDistrict();
		int councilorCount = user.getCityAddress().councilorCount() / user.getCityAddress().getDistrictCount();
		out.println(gson.toJson(db.getCandidates(electionID, district, councilorCount)));
		out.flush();
	}
	
	// Submits the vote and adds it to the database.
	private void submitVote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		List<String> candidateIDs = new ArrayList<>();
		String electionID = request.getParameter("electionID");
		String userID = request.getParameter("userID");
		Collections.list(request.getParameterNames()).stream().forEach((x) -> {
			if (x.contains("id")) {
				candidateIDs.add(request.getParameter(x));
			} 
		});
		String referenceNumber = db.castVote(candidateIDs, electionID, userID);
		
		if (referenceNumber != null) {
			out.println(String.format("{\"status\": \"success\", \"referenceNumber\": \"%s\"}", referenceNumber));
			out.flush();
		} else {
			out.println("{\"status\": \"error\"}");
			out.flush();
		}

	}
}
