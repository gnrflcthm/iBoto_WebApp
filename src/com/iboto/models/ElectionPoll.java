package com.iboto.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElectionPoll {
	private String electionID;
	private List<MayorCandidate> mayors;
	private List<ViceMayorCandidate> viceMayors;
	private List<CouncilorCandidate> councilors;
	
	public ElectionPoll(String electionID, 
						List<MayorCandidate> mayors, 
						List<ViceMayorCandidate> viceMayors,
						List<CouncilorCandidate> councilors) {
		this.electionID = electionID;
		this.mayors = mayors;
		this.viceMayors = viceMayors;
		this.councilors = councilors;
	}

	public String getElectionID() {
		return electionID;
	}

	public List<MayorCandidate> getMayors() {
		return mayors;
	}

	public List<ViceMayorCandidate> getViceMayors() {
		return viceMayors;
	}

	public List<CouncilorCandidate> getCouncilors() {
		return councilors;
	}
	
	public static ElectionPoll loadFromDatabase(String electionID, ResultSet res) throws SQLException {
		List<MayorCandidate> mayors = new ArrayList<>();
		List<ViceMayorCandidate> viceMayors = new ArrayList<>();
		List<CouncilorCandidate> councilors = new ArrayList<>();
		while (res.next()) {
			String position = res.getString(3);
			String name = res.getString(1);
			String id = res.getString(2);
			String votes = res.getString(4);
			switch(position) {
				case "Mayor":
					mayors.add(new MayorCandidate(id, name, Integer.valueOf(votes)));
					break;
				case "Vice Mayor":
					viceMayors.add(new ViceMayorCandidate(id, name, Integer.valueOf(votes)));
					break;
				case "Councilor":
					councilors.add(new CouncilorCandidate(id, name, votes));
					break;
			}
		}
		return new ElectionPoll(electionID, mayors, viceMayors, councilors);
	}
	
	
}
