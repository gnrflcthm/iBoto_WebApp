package com.iboto.models;

public class ViceMayorCandidate extends Candidate {
	
	public static final String POSITION = "VICE MAYOR";
	
	public ViceMayorCandidate(String id, String name) {
		super(id, name);
	}
	
	public ViceMayorCandidate(String id, String name, Platform[] platforms) {
		super(id, name, platforms);
	}
}
