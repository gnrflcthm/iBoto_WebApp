package com.iboto.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.iboto.constants.City;
import com.thiam.encryption.HashUtils;

public class IBotoDbUtils {
	
	private final String USER;
	private final String PASSWORD;
	private final String DRIVER;
	private static String URL = "jdbc:mysql://localhost:3306/evotingdb";
	private Connection conn;
	
	public IBotoDbUtils(ServletContext context) {
		this.USER = context.getInitParameter("user");
		this.PASSWORD = context.getInitParameter("password");
		this.DRIVER = context.getInitParameter("driver");
	}
	
	public static IBotoDbUtils getInstance(ServletContext context) {
		return new IBotoDbUtils(context);
	}
	
	public boolean addUser(String email, String phoneNum, String lastName, String firstName, String birthday, City address, int district, String password) {
		try {
			if (connect()) {	
				String userID = generateUserID(address);
				String[] pwd = HashUtils.hash(password);
				String sql = "INSERT INTO voter(UserID, Email, PhoneNumber, LastName, FirstName, DateOfBirth, Address, District, UPassword, Password_Salt)" +
							 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement s = conn.prepareStatement(sql);
				s.setString(1, userID);
				s.setString(2, email);
				s.setString(3, phoneNum);
				s.setString(4, lastName);
				s.setString(5, firstName);
				s.setString(6, birthday);
				s.setString(7, address.getProperName());
				s.setInt(8, district);
				s.setString(9, pwd[0]);
				s.setString(10, pwd[1]);
				
				s.executeUpdate();
				
				if (s != null && !s.isClosed()) {
					s.close();
				}
				
				disconnect();
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean validateUserLogin(String user, String password) {
		String[] pwd = new String[2];
		try {
			if (connect()) {
				String sql = "SELECT UPassword, Password_Salt FROM voter WHERE UserID = ? OR Email = ?";
				PreparedStatement s = conn.prepareStatement(sql);
				s.setString(1, user);
				s.setString(2, user);
				ResultSet res = s.executeQuery();
				res.next();
				pwd = new String[] {res.getString(1), res.getString(2)};
				if (res != null && !res.isClosed()) {
					res.close();
				}
				if (s != null && !s.isClosed()) {
					s.close();
				}
				disconnect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return HashUtils.validateText(pwd[0], password, pwd[1]);
	}
	
	private String generateUserID(City city) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT COUNT(*) FROM voter WHERE UserID LIKE '" + city.getCityCode() + "%'");
		res.next();
		int userCount = res.getInt(1);
		if (res != null && !res.isClosed()) {
			res.close();
		}
		return generateID(city, "V", userCount);
	}
	
	/**
	 * Generic method for generating IDs
	 * @param city uses city code as prefix for the generated ID.
	 * @param type string literal for id type ("V" - voter, "C" - candidate, "AD" - admin)
	 * @param count total records based on table records
	 * @return generated ID based on input parameters
	 */
	private String generateID(City city, String type, int count) {
		StringBuilder id = new StringBuilder();
		id.append(city.getCityCode() + type + String.format("%06d", count));
		return id.toString();
	}
	
	private boolean connect() {
		try {		
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean disconnect() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
			return true;
		}
		return false;
	}
}
