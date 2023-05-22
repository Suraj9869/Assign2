package com.hrm.assign.entity;

import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.validator.BirthDate;
import com.hrm.assign.validator.HireDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer employeeId;
	
	@Column(unique = true)
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
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	@HireDate
	private Date hireDate;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager managerReports;

	
	

}
