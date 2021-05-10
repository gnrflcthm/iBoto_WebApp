package com.iboto.models;

public class Candidate {
	private String id;
	private String name;
	private Platform[] platforms;
	
	public Candidate(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Candidate(String id, String name, Platform[] platforms) {
		this(id, name);
		this.platforms = platforms;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Platform[] getPlatforms() {
		return this.platforms;
	}
}
