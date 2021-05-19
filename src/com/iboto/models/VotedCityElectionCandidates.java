package com.iboto.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VotedCityElectionCandidates {
	private MayorCandidate mayor;
	private ViceMayorCandidate viceMayor;
	private List<CouncilorCandidate> councilorCandidates;
	
	public VotedCityElectionCandidates (MayorCandidate mayorCandidates, 
										ViceMayorCandidate viceMayorCandidates, 
										List<CouncilorCandidate> councilorCandidates) {
		this.mayor= mayorCandidates;
		this.viceMayor= viceMayorCandidates;
		this.councilorCandidates = councilorCandidates;
	}
	
	public MayorCandidate getMayorCandidate() {
		return mayor;
	}

	public ViceMayorCandidate getViceMayorCandidate() {
		return viceMayor;
	}

	public List<CouncilorCandidate> getCouncilorCandidates() {
		return councilorCandidates;
	}
	
	public static VotedCityElectionCandidates loadFromDatabase(ResultSet res) {
		MayorCandidate mayor= null;
		ViceMayorCandidate viceMayor = null;
		List<CouncilorCandidate> councilorCandidates = new ArrayList<>();
		try {
			while (res.next()) {
				String position = res.getString("Position");
				String name = res.getString("CandidateName");
				String id = res.getString("CandidateID");
				int dist = res.getInt("District");
				
				switch(position) {
					case "Mayor":
						mayor = new MayorCandidate(id, name);
						break;
					case "Vice Mayor":
						viceMayor = new ViceMayorCandidate(id, name);
						break;
					case "Councilor":
						councilorCandidates.add(new CouncilorCandidate(id, name, dist));
						break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new VotedCityElectionCandidates(mayor, viceMayor, councilorCandidates);
	}
}
