package com.iboto.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.iboto.constants.City;
import com.iboto.models.CityElectionCandidates;
import com.iboto.models.Election;
import com.iboto.models.ElectionPoll;
import com.iboto.models.UserBean;
import com.iboto.models.UserVoteSummary;
import com.iboto.models.UserVotes;
import com.iboto.models.VotedCityElectionCandidates;
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
	
	/**
	 * Retrieves user data from the database according to the input ID
	 * then creates and returns an instance of User Bean
	 * @param id user ID or email
	 * @return UserBean representing a user
	 */
	public UserBean getUserInstance(String id) {
		UserBean user;
		try {
			if (connect()) {
				PreparedStatement s = conn.prepareStatement("SELECT * FROM voter WHERE UserID = ? OR Email = ?");
				s.setString(1, id);
				s.setString(2, id);
				ResultSet res = s.executeQuery();
				res.next();
				String userID = res.getString("UserID");
				String email = res.getString("Email");
				String phoneNum = res.getString("PhoneNumber");
				String lastName = res.getString("LastName");
				String firstName = res.getString("FirstName");
				LocalDate birthday = LocalDate.parse(res.getString("DateOfBirth"));
				City cityAddress = City.getByProperName(res.getString("Address"));
				int district = res.getInt("District");
				user = UserBean.getInstance(userID, email, phoneNum, lastName, firstName, birthday, cityAddress, district);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Inserts a new User to the database using the given information.
	 * @param email 
	 * @param phoneNum
	 * @param lastName
	 * @param firstName
	 * @param birthday
	 * @param address
	 * @param district
	 * @param password
	 * @return true if user has been successfully added  to the database 
	 * 		   otherwise false
	 */
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
	
	/**
	 * Validates if the input userID or email, and password are valid
	 * @param user user ID or email
	 * @param password password
	 * @return true if user exists and password matches the given password
	 *    	   otherwise false if user does not exist or password is incorrect
	 */
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
			System.out.println("Error!");
			return false;
		}
		return HashUtils.validateText(pwd[0], password, pwd[1]);
	}
	
	/**
	 * Generates UserID based on the users city address.
	 * @param city City Address
	 * @return UserID
	 * @throws SQLException
	 */
	private String generateUserID(City city) throws SQLException {
		ResultSet res = conn.createStatement().executeQuery("SELECT UserID FROM voter WHERE UserID LIKE '" + city.getCityCode() + "%' ORDER BY UserID DESC LIMIT 1");
		res.next();
		String lastID = res.getString("UserID");
		int userCount = Integer.valueOf(lastID.substring(10)) + 1;
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
	
	/**
	 * Establishes connection with database using the given credentials.
	 * @return true if connection is established without errors otherwise false
	 */
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
	
	/**
	 * Disconnects the database.
	 * @return true if database disconnected successfully otherwise false.
	 * @throws SQLException
	 */
	public boolean disconnect() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
			return true;
		}
		return false;
	}
	
	public List<Election> getElections(City city) {
		List<Election> elections = new ArrayList<>();
		try {
			if (connect()) {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM election WHERE City = ?");
				ps.setString(1, city.getProperName());
				ResultSet res = ps.executeQuery();
				while (res.next()) {
					String id = res.getString(1);
					String name = res.getString(2);
					String cityAddress = res.getString(3);
					Date dateStart = res.getDate(4);
					Date dateEnd = res.getDate(5);
					elections.add(new Election(id, name, cityAddress, dateStart, dateEnd));
				}
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elections;
	}
	
	public CityElectionCandidates getCandidates(String electionID, int district, int councilorCount) {
		CityElectionCandidates candidates = null;
		try {
			if (connect()) {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM candidate WHERE ElectionID = ?");
				ps.setString(1, electionID);
				candidates = CityElectionCandidates.loadFromDatabase(ps.executeQuery(), district, councilorCount);
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		getPlatforms(electionID, candidates);
		return candidates;
	}
	
	private void getPlatforms(String electionID, CityElectionCandidates candidates) {
		try {
			if (connect()) {
				String sql = "SELECT * FROM evotingdb.platform " + 
							 "WHERE CandidateID " + 
							 "IN (SELECT CandidateID FROM evotingdb.candidate WHERE ElectionID = ?);";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, electionID);
				ResultSet platforms = ps.executeQuery();
				CityElectionCandidates.loadPlatforms(candidates.getMayorCandidates(),
													 candidates.getViceMayorCandidates(),
													 candidates.getCouncilorCandidates(),
													 platforms);
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Date getVoted(String userID, String electionID) {
		Date dateVoted = null;
		try {
			if (connect()) {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM vote WHERE VoterID = ? AND ElectionID = ?");
				ps.setString(1, userID);
				ps.setString(2, electionID);
				ResultSet res = ps.executeQuery();
				
				if (res.next()) {
					dateVoted = res.getDate("DateVoted");
				}
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateVoted;
	}
	
	public String castVote(List<String> candidateIDs, String electionID, String userID) {
		try {
			String voteID = addVote(userID, electionID);
			if (connect()) {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO votedcandidate VALUES (?, ?)");
				candidateIDs.forEach((id) -> {
					try {
						ps.setString(1, voteID);
						ps.setString(2, id);
						ps.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				disconnect();
				return voteID;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String addVote(String userID, String electionID) {
		String voteID = userID.substring(10) + electionID + LocalDate.now().toString().replace("-", "");
		try {
			if (connect()) {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO vote VALUES (?, ?, ?, ?, ?)");
				ps.setString(1, voteID);
				ps.setString(2, userID);
				ps.setString(3, electionID);
				ps.setInt(4, 1);
				ps.setString(5, LocalDate.now().toString());
				ps.execute();
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voteID;
	}
	
	public UserVotes getUserVotes(String userID) {
		UserVotes userVotes = null;
		try {
			if (connect()) {
				String sql = "SELECT VoteId, VoterID, DateVoted, election.ElectionID, election.ElectionName " + 
							 "FROM vote " + 
							 "JOIN election ON vote.electionID = election.ElectionID " + 
							 "WHERE VoterID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1,  userID);
				ResultSet res = ps.executeQuery();
				userVotes = UserVotes.loadFromDatabase(res, userID);
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userVotes;
	}
	
	public VotedCityElectionCandidates getVotedCandidates(String voteID) {
		VotedCityElectionCandidates votedCandidates = null;
		try {
			if (connect()) {
				String sql = "SELECT VoteID, votedcandidate.CandidateID, candidate.CandidateID, candidate.CandidateName, candidate.District, candidate.Position " + 
							 "FROM votedcandidate " + 
							 "JOIN candidate ON votedcandidate.CandidateID = candidate.CandidateID " + 
							 "WHERE VoteID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, voteID);
				votedCandidates = VotedCityElectionCandidates.loadFromDatabase(ps.executeQuery());
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return votedCandidates;
	}
	
	public UserVoteSummary getVoteSummary(String voteID) {
		UserVoteSummary voteSummary = null;
		try {
			if (connect()) {
				String sql = "SELECT VoteId, VoterID, DateVoted, election.ElectionID, election.ElectionName " + 
							 "FROM vote " + 
							 "JOIN election ON vote.ElectionID = election.ElectionID " + 
							 "WHERE VoteID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1,  voteID);
				ResultSet res = ps.executeQuery();
				res.next();
				String userID = res.getString("VoterID");
				String electionID = res.getString("ElectionID");
				String electionName = res.getString("ElectionName");
				Date dateVoted = res.getDate("DateVoted");
				voteSummary = new UserVoteSummary(userID, electionID, voteID, electionName, dateVoted);
				disconnect();				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voteSummary;
	}
	
	public ElectionPoll getElectionPoll(String electionID) {
		ElectionPoll electionPoll = null;
		try {
			if (connect()) {
				String sql = "SELECT candidate.candidatename, candidate.candidateid, candidate.position, COUNT(votedcandidate.CandidateID) as Poll " + 
							 "FROM candidate " + 
							 "LEFT JOIN votedcandidate ON votedcandidate.CandidateID = candidate.CandidateID " + 
							 "WHERE candidate.ElectionID = ? " + 
							 "GROUP BY candidate.candidateid " + 
							 "ORDER BY Poll DESC";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, electionID);
				electionPoll = ElectionPoll.loadFromDatabase(electionID, ps.executeQuery());
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return electionPoll;
	}

	public void updatePassword(String userID, String npw) {
		try {
			if (connect()) {
				String sql = "UPDATE voter SET UPassword = ?, Password_Salt = ? WHERE UserID = ?";
				String[] pwd = HashUtils.hash(npw);
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, pwd[0]);
				ps.setString(2, pwd[1]);
				ps.setString(3, userID);
				ps.executeUpdate();
				disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
