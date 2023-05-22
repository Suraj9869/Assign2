package com.hrm.assign.validator;

import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

import com.hrm.assign.entity.Employee;
import com.hrm.assign.exception.InvalidFieldException;

public class ValidateInput {

	public static boolean validateEmailId(String emailId) throws InvalidFieldException {
		
		boolean validEmail = Pattern.matches("[a-zA-Z0-9]+@capgemini\\.com", emailId);
		
		if(validEmail) {
			return true;
		}
		
		throw new InvalidFieldException("Invalid Email Id passed");
		
	}
	
	public static boolean validateBirthDate(Date birthDate) throws InvalidFieldException {
		
		int birthYear = birthDate.getYear();
		int currentYear = LocalDate.now().getYear();
		int diffYear = currentYear - birthYear;
		
		if(diffYear >= 20) {
			return true;
		}
		
		throw new InvalidFieldException("Date of Birth is not valid");
		
	}
	
	public static boolean validateHireDate(Date hireDate) throws InvalidFieldException {
		
		LocalDate hiredDate = LocalDate.of(hireDate.getYear(), hireDate.getMonth(), hireDate.getDay());
		
		if(hiredDate.isBefore(LocalDate.now())) {
			return true;
		}
		
		throw new InvalidFieldException("Hired date is greated than a current date");
		
	}
	
	public boolean validateEmployeeDetails(Employee employee) throws InvalidFieldException {
		
		String emailId = employee.getEmailId();
		Date birthDate = employee.getBirthDate();
		Date hireDate = employee.getHireDate();
		
		validateEmailId(emailId);
		validateBirthDate(birthDate); 
		validateHireDate(hireDate);
		
		return true;
		
	}
}
