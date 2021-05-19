package com.iboto.models;

import java.util.List;

public class MayorCandidate extends Candidate {
	
	public static final String POSITION = "MAYOR";
	
	public MayorCandidate(String id, String name) {
		super(id, name);
	}
	
	public MayorCandidate(String id, String name, int voteCount) {
		super(id, name, voteCount);
	}
	
	public MayorCandidate(String id, String name, List<Platform> platforms) {
		super(id ,name, platforms);
	}
}
