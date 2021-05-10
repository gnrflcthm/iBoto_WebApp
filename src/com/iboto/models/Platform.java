package com.iboto.models;

public class Platform {
	private String name;
	private String description;
	
	public Platform(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
}
