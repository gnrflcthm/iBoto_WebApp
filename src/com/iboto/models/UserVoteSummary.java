package com.iboto.models;

import java.sql.Date;
import java.util.List;

public class UserVoteSummary {
	private String userID;
	private String electionID;
	private String referenceNumber;
	private String electionName;
	private Date dateVoted;
	
	public UserVoteSummary(String userID, String electionID, String referenceNumber, String electionName, Date dateVoted) {
		this.userID = userID;
		this.electionID = electionID;
		this.referenceNumber = referenceNumber;
		this.electionName = electionName;
		this.dateVoted = dateVoted;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getElectionID() {
		return electionID;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}
	
	public String getElectionName() {
		return electionName;
	}
	
	public Date getDateVoted() {
		return dateVoted;
	}

}
