package com.iboto.models;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserVotes {
	private String userID;
	private List<UserVoteSummary> votedElections;
	
	public UserVotes(String userID) {
		this.userID = userID;
		this.votedElections = new ArrayList<>();
	}
	
	public String getUserID() {
		return userID;
	}
	
	public List<UserVoteSummary> getVotedElections() {
		return votedElections;
	}
	
	public static UserVotes loadFromDatabase(ResultSet votes, String userID) {
		UserVotes userVotes = new UserVotes(userID);
		try {
			while (votes.next()) {
				String electionID = votes.getString("ElectionID");
				String referenceNumber = votes.getString("VoteID");
				String electionName = votes.getString("ElectionName");
				Date dateVoted = votes.getDate("DateVoted");
				userVotes.getVotedElections().add(new UserVoteSummary(userID, electionID, referenceNumber, electionName, dateVoted));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userVotes;
	}
}
