package com.hrm.assign.entity;

import java.util.ArrayList;


import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude="employeesList")
public class Manager {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer managerId;
	
	@Column(unique = true)
	@Pattern(regexp = "[a-zA-Z0-9]+@anzz\\.com", message = "Please enter ANZZ provided emailId")
	private String emailId;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date fromDate;
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date toDate;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "managerReports")
	private List<Employee> employeesList = new ArrayList<>();

	
	
}
