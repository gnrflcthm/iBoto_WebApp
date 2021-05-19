package com.iboto.models;

import java.util.List;

public class ViceMayorCandidate extends Candidate {
	
	public static final String POSITION = "VICE MAYOR";
	
	public ViceMayorCandidate(String id, String name) {
		super(id, name);
	}
	
	public ViceMayorCandidate(String id, String name, List<Platform> platforms) {
		super(id, name, platforms);
	}
}
