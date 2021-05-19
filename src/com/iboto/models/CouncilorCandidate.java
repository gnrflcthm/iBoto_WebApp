package com.iboto.models;

import java.util.List;

public class CouncilorCandidate extends Candidate {
	
	private int district;
	
	public CouncilorCandidate(String id, String name, int district) {
		super(id, name);
		this.district = district;
	}
	
	public CouncilorCandidate(String id, String name, String voteCount) {
		super(id, name, Integer.valueOf(voteCount));
	}
	
	public CouncilorCandidate(String id, String name, List<Platform> platforms, int district) {
		super(id, name, platforms);
		this.district = district;
	}
	
	public int getDistrict() {
		return this.district;
	}
}
