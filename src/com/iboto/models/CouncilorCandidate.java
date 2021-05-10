package com.iboto.models;

public class CouncilorCandidate extends Candidate {
	
	private int district;
	
	public CouncilorCandidate(String id, String name, int district) {
		super(id, name);
		this.district = district;
	}
	
	public CouncilorCandidate(String id, String name, Platform[] platforms, int district) {
		super(id, name, platforms);
		this.district = district;
	}
	
	public int getDistrict() {
		return this.district;
	}
}
