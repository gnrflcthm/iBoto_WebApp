package com.iboto.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iboto.constants.City;

/**
 * Servlet implementation class RegistrationValidatorServlet
 */
@WebServlet({"/regvalidate.jsp" })
public class RegistrationValidatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationValidatorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int segment = Integer.valueOf(request.getParameter("validate"));
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		switch(segment) {
			case 0:
				String city = request.getParameter("city");
				int districtCount = City.valueOf(city).getDistrictCount();
				if (validateName(request)) {
					out.println(String.format("{\"valid\": true, \"district_count\": %d}", districtCount));
					out.flush();
				} else {
					out.println("{\"valid\": false}");
					out.flush();
				}
				break;
			case 1:
				boolean validBirthday = validateBirthday(request);
				boolean validPassword = validatePassword(request);
				boolean validEmail    = validateEmail(request);
				boolean validPhoneNum = validatePhoneNum(request);
				out.println(String.format("{\"birthday\": %s, \"password\": %s, \"email\": %s, \"phoneNum\": %s}", validBirthday, validPassword, validEmail, validPhoneNum));
				out.flush();
				break;
		}
	}
	
	private boolean validateName(HttpServletRequest req) {
		String fName = req.getParameter("fname").strip();
		String mName = req.getParameter("mname").strip();
		String lName = req.getParameter("lname").strip();
		return fName.length() > 0 && mName.length() > 0 && lName.length() > 0;
	}
	
	private boolean validateBirthday(HttpServletRequest req) {
		if (req.getParameter("bday").equals("") || req.getParameter("bday") == null) {
			return false;
		}
		int[] date = Stream.of(req.getParameter("bday").split("-")).mapToInt((x) -> Integer.valueOf(x)).toArray();
		LocalDate birthday = LocalDate.of(date[0], date[1], date[2]);
		int age = LocalDate.now().getYear() - birthday.getYear();
		if (LocalDate.now().getMonthValue() <= birthday.getMonthValue()) {
			if (LocalDate.now().getDayOfMonth() < birthday.getDayOfMonth()) {
				age -= 1;
			}
		}
		if (age < 18) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean validatePassword(HttpServletRequest req) {
		String pwd = req.getParameter("pwd");
		String cnpwd = req.getParameter("cnpwd");
		int ucase = countUppercase(pwd);
		return (pwd.length() > 6 && pwd.equals(cnpwd) && ucase > 0);
	}
	
	private int countUppercase(String text) {
		char[] temp = text.toCharArray();
		int upperCount = 0;
		for (char x : temp) {
			upperCount += (Character.isUpperCase(x)) ? 1 : 0;
		}
		return upperCount;
	}
	
	private boolean validateEmail(HttpServletRequest req) {
		String email = req.getParameter("email");
		if (email.equals("")) {
			return true;
		} else {
			return email.matches(".+@.+");
		}
	}
	
	private boolean validatePhoneNum(HttpServletRequest req) {
		String phoneNum = req.getParameter("phoneNum");
		if (phoneNum.equals("")) {
			return true;
		} else {
			return phoneNum.matches("(\\d{11}|\\+63\\d{10})");
		}
	}
}
