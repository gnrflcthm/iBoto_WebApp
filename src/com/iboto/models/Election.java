package com.iboto.models;

import java.sql.Date;

public class Election {
	private String id;
	private String name;
	private String city;
	private Date dateStart;
	private Date dateEnd;
	
	public Election(String id, String name, String city, Date dateStart, Date dateEnd) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getId() {
		return id;
	}

	public String getCity() {
		return city;
	}
	
	
}
