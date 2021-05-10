package com.iboto.models;

public class MayorCandidate extends Candidate {
	
	public static final String POSITION = "MAYOR";
	
	public MayorCandidate(String id, String name) {
		super(id, name);
	}
	
	public MayorCandidate(String id, String name, Platform[] platforms) {
		super(id ,name, platforms);
	}
}
