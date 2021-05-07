package com.iboto.models;

import java.time.LocalDate;

import com.iboto.constants.City;

public class UserBean {
	private String userID;
	private String email;
	private String phoneNum;
	private String lastName;
	private String firstName;
	private LocalDate birthday;
	private City cityAddress;
	private int district;
	
	public UserBean() {}

	public UserBean(String userID, String email, String phoneNum, String lastName, String firstName, LocalDate birthday,
			City cityAddress, int district) {
		super();
		this.userID = userID;
		this.email = email;
		this.phoneNum = phoneNum;
		this.lastName = lastName;
		this.firstName = firstName;
		this.birthday = birthday;
		this.cityAddress = cityAddress;
		this.district = district;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public City getCityAddress() {
		return cityAddress;
	}

	public void setCityAddress(City cityAddress) {
		this.cityAddress = cityAddress;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}
	
	/**
	 * Creates and returns an instance of UserBean given the parameters.
	 * @param userID
	 * @param email
	 * @param phoneNum
	 * @param lastName
	 * @param firstName
	 * @param birthday
	 * @param cityAddress
	 * @param district
	 * @return UserBean instance.
	 */
	public static UserBean getInstance(String userID, String email, String phoneNum, String lastName, String firstName, LocalDate birthday,
			City cityAddress, int district) {
		return new UserBean(userID, email, phoneNum, lastName, firstName, birthday, cityAddress, district);
	}
	
}
