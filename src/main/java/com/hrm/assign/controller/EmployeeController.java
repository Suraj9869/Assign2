package com.hrm.assign.controller;


import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.helper.DecryptJwtToken;
import com.hrm.assign.service.EmployeeService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	Logger log = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DecryptJwtToken decryptJwtToken;
	
	
	
	/**
	 * Employee fetch his personal details
	 * 
	 * @param emailId @type String @description EmailId of the employee
	 * @return Details of the Employee
	 */
	@GetMapping("/getMyDetails")
	@RolesAllowed("ROLE_EMPLOYEE")
	public ResponseEntity<Employee> getMyDetails(HttpServletRequest request){
		
		String emailId = decryptJwtToken.decryptEmailId(request);
		
		log.info("Employee requested for his details");
		
		Employee employeeDetails = employeeService.getMyDetails(emailId);
		
		log.info("Employee got his details");
		log.debug("Employee fetched his detials {}", employeeDetails);
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
	}
	
	/**
	 * Create an employee if request is raised by admin role
	 * 
	 * @param employee @type Employee @description Details of employee to be registered
	 * @return Employee details DTO that was saved
	 */
	@PostMapping("/signUp")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<EmployeeDto> signUpEmployee(@Valid @RequestBody Employee employee) {
		
		EmployeeDto employeeDetails = employeeService.registerEmployee(employee);
		
		log.info("Admin added employee in to the database");
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * @param employee @type Employee Employee details 
	 * @return Updated employee details
	 */
	
	@PutMapping("/updateMyDetails")
	@RolesAllowed("ROLE_EMPLOYEE")
	public ResponseEntity<EmployeeDto> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto){
		
		EmployeeDto employeeDetails = employeeService.updateEmployee(employeeDto);
		
		log.info("Employee updated his details in to the database");
		log.debug("Employee Update his profile {}", employeeDetails);
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * Deletes the employee from the database
	 * 
	 * @param emailId @type String Employee emailId
	 * @return Success or Fail message 
	 */
	@DeleteMapping("/deleteEmployee")
	@RolesAllowed("ROLE_EMPLOYEE")
	public ResponseEntity<String> deleteEmployee(HttpServletRequest request){
		
		String emailId = decryptJwtToken.decryptEmailId(request);
		
		boolean emplDelete = employeeService.deleteEmployee(emailId);
		
		if(emplDelete) {
			log.info("Employee successfully deleted his profile");
			log.debug("Employee profile deleted from database {}", emailId);
			return new ResponseEntity<>("Employee Successfully Deleted", HttpStatus.ACCEPTED);
		}
		else {
			log.info("Unable to delete the employee details");
			log.debug("Unable to delete employee detials of {}", emailId);
			return new ResponseEntity<>("Unable to delete Employee", HttpStatus.BAD_REQUEST);
		}
		
	}
	
}
