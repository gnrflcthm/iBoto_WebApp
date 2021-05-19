package com.iboto.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iboto.database.IBotoDbUtils;

public class CityElectionCandidates {
	private List<MayorCandidate> mayorCandidates;
	private List<ViceMayorCandidate> viceMayorCandidates;
	private List<CouncilorCandidate> councilorCandidates;
	private int councilorCount;
	
	public CityElectionCandidates(List<MayorCandidate> mayorCandidates, 
			List<ViceMayorCandidate> viceMayorCandidates, 
			List<CouncilorCandidate> councilorCandidates, int councilorCount) {
		this.mayorCandidates = mayorCandidates;
		this.viceMayorCandidates = viceMayorCandidates;
		this.councilorCandidates = councilorCandidates;
		this.councilorCount = councilorCount;
	}

	public List<MayorCandidate> getMayorCandidates() {
		return mayorCandidates;
	}

	public List<ViceMayorCandidate> getViceMayorCandidates() {
		return viceMayorCandidates;
	}

	public List<CouncilorCandidate> getCouncilorCandidates() {
		return councilorCandidates;
	}
	
	public int getCouncilorCount() {
		return councilorCount;
	}

	public static CityElectionCandidates loadFromDatabase(ResultSet res, int district, int councilorCount) {
		List<MayorCandidate> mayorCandidates = new ArrayList<>();
		List<ViceMayorCandidate> viceMayorCandidates = new ArrayList<>();
		List<CouncilorCandidate> councilorCandidates = new ArrayList<>();
		try {
			while (res.next()) {
				String position = res.getString("Position");
				String name = res.getString("CandidateName");
				String id = res.getString("CandidateID");
				int dist = res.getInt("District");
				
				switch(position) {
					case "Mayor":
						mayorCandidates.add(new MayorCandidate(id, name));
						break;
					case "Vice Mayor":
						viceMayorCandidates.add(new ViceMayorCandidate(id, name));
						break;
					case "Councilor":
						if (dist != district) {
							break;
						}
						councilorCandidates.add(new CouncilorCandidate(id, name, dist));
						break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new CityElectionCandidates(mayorCandidates, viceMayorCandidates, councilorCandidates, councilorCount);
	}
	
	public static void loadPlatforms(List<MayorCandidate> mayors, List<ViceMayorCandidate> viceMayors, List<CouncilorCandidate> councilors, ResultSet platforms) {
		HashMap<String, Candidate> candidates = new HashMap<>();
		mayors.forEach((x) -> candidates.put(x.getId(), x));
		viceMayors.forEach((x) -> candidates.put(x.getId(), x));
		councilors.forEach((x) -> candidates.put(x.getId(), x));
		try {			
			while (platforms.next()) {
				String name = platforms.getString("Title");
				String desc = platforms.getString("PlatDesc");
				Platform p = new Platform(name, desc);
				String candidateID = platforms.getString("CandidateID");
				candidates.get(candidateID).getPlatforms().add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
