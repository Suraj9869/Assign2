package com.hrm.assign.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.hrm.assign.enums.Gender;
import com.hrm.assign.validator.BirthDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]+@anzz\\.com", message = "Please enter ANZZ provided emailId")
	private String emailId;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	private Gender gender;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@BirthDate(message = "The birth date must be greater or equal than 20")
	private Date birthDate;
	
	@NotBlank
	private String address;
	
	@NotBlank
	@Pattern(regexp = "[1-9]{1}[0-9]{9}" , message = "Please enter a correct phone number")
	private String phoneNumber;

	@Override
	public String toString() {
		return "EmployeeDto [emailId=" + emailId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", birthDate=" + birthDate + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
	}
	
	
	
}
