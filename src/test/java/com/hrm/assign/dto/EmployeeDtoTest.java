package com.hrm.assign.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hrm.assign.enums.Gender;

class EmployeeDtoTest {

	private EmployeeDto employeeDto;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	void testNoArgConstructor() throws ParseException {
		
		
		employeeDto = new EmployeeDto();
		
		String emailId = "alice@anzz.com";
		String firstName = "Alice";
		String lastName = "Saw";
		Date birthDate = sdf.parse("2001-09-05");
		String address = "Chowk Delhi";
		String phoneNumber = "8858684868";
		
		employeeDto.setEmailId(emailId);
		employeeDto.setFirstName(firstName);
		employeeDto.setLastName(lastName);
		employeeDto.setBirthDate(birthDate);
		employeeDto.setAddress(address);
		employeeDto.setPhoneNumber(phoneNumber);
		
		assertEquals(emailId, employeeDto.getEmailId());
		assertEquals(firstName, employeeDto.getFirstName());
		assertEquals(lastName, employeeDto.getLastName());
		assertEquals(birthDate, employeeDto.getBirthDate());
		assertEquals(address, employeeDto.getAddress());
		assertEquals(phoneNumber, employeeDto.getPhoneNumber());
		
	}
	
	void testAllArgContructor() throws ParseException {
		
		String emailId = "alice@anzz.com";
		String firstName = "Alice";
		String lastName = "Saw";
		Date birthDate = sdf.parse("2001-09-05");
		String address = "Chowk Delhi";
		String phoneNumber = "8858684868";
		Gender gender = Gender.Male;
		employeeDto = new EmployeeDto(emailId, firstName, lastName, gender, birthDate, address, phoneNumber);
		
		assertEquals(emailId, employeeDto.getEmailId());
		assertEquals(firstName, employeeDto.getFirstName());
		assertEquals(lastName, employeeDto.getLastName());
		assertEquals(birthDate, employeeDto.getBirthDate());
		assertEquals(address, employeeDto.getAddress());
		assertEquals(phoneNumber, employeeDto.getPhoneNumber());
		
	}
}
