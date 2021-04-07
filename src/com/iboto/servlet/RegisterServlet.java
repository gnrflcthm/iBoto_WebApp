package com.iboto.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iboto.constants.City;
import com.iboto.database.IBotoDbUtils;
import com.thiam.encryption.HashUtils;
/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String middleName;		
		IBotoDbUtils db = new IBotoDbUtils(this.getServletContext());
		String email = request.getParameter("email");
		String phoneNum = request.getParameter("phoneNum");
		String lastName = request.getParameter("lname");
		String firstName = request.getParameter("fname");
		Date birthday = parseDate(request.getParameter("birthday"));
		City address = City.valueOf(request.getParameter("city"));
		int district = Integer.valueOf(request.getParameter("district"));
		String password = request.getParameter("password");
		
		db.addUser(email, phoneNum, lastName, firstName, birthday, address, district, password);
		response.sendRedirect("home.jsp");
	}
	
	private Date parseDate(String date) {
		int[] data = Stream.of(date.split("-")).mapToInt((x) -> Integer.valueOf(x)).toArray();
		return new Date(data[0], data[1], data[2]);
	}
	
	public static void main(String[] args) {
		String password = "password";
		String[] passwordHash = HashUtils.hash(password);
	}
}
