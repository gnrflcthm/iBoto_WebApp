package com.iboto.models;
import java.util.List;
import java.util.ArrayList;

public class Candidate {
	private String id;
	private String name;
	private List<Platform> platforms;
	
	public Candidate(String id, String name) {
		this.id = id;
		this.name = name;
		platforms = new ArrayList<>();
	}
	
	public Candidate(String id, String name, List<Platform> platforms) {
		this(id, name);
		this.platforms = platforms;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public List<Platform> getPlatforms() {
		return this.platforms;
	}
}
