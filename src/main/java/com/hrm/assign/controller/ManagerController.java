package com.hrm.assign.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.helper.DecryptJwtToken;
import com.hrm.assign.service.ManagerService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	ManagerService managerService;
	
	@Autowired
	DecryptJwtToken decryptJwtToken;
	
	Logger log = LoggerFactory.getLogger(ManagerController.class);
	
	/**
	 * 
	 * @param emailId @type String @description EmailId of the manager
	 * @return Details of the manager
	 */
	@GetMapping("/getMyDetails")
	@RolesAllowed("ROLE_MANAGER")
	public ResponseEntity<Manager> getMyDetails(HttpServletRequest request){
		
		String emailId = decryptJwtToken.decryptEmailId(request);
		
		log.info("Manager requested for his details");
		
		Manager managerDetails = managerService.getMyDetails(emailId);
		
		log.info("Manager got his details");
		log.debug("Manager fetched his detials {}", managerDetails);
		
		return new ResponseEntity<>(managerDetails, HttpStatus.ACCEPTED);
	}
	
	/**
	 * Create an employee if request is raised by admin role
	 * 
	 * @param employee @type Manager @description Details of manager to be registered
	 * @return Manager details DTO that was saved
	 */
	@PostMapping("/signUp")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<Manager> signUpManager(@Valid @RequestBody Manager manager){
		
		Manager managerDetails = managerService.signUp(manager);
		
		log.info("Admin added manager details in to the database");
		
		return new ResponseEntity<>(managerDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * @param employee @type Manager Manager details 
	 * @return Updated manager details
	 */
	@PutMapping("/updateMyDetails")
	@RolesAllowed("ROLE_MANAGER")
	public ResponseEntity<Manager> updateMyDetails(@RequestBody Manager manager){
		
		Manager managerDetails = managerService.updateDetails(manager);
		
		log.info("Manager updated his details in to the database");
		log.debug("Manager Updated his profile {}", managerDetails);
		
		return new ResponseEntity<>(managerDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * @param emailId @type String @description EmailId of the manager
	 * @return List of employees that are under manager
	 */
	@GetMapping("/getMyEmployee")
	@RolesAllowed("ROLE_MANAGER")
	public ResponseEntity<List<Employee>> getMyEmployees(HttpServletRequest request){
		
		String emailId = decryptJwtToken.decryptEmailId(request);
		
		List<Employee> myEmployees = managerService.listMyEmployees(emailId);
		
		log.info("Manager got the details of employees working under him");
		log.debug("Manager fetched his employees detials {}", myEmployees);
		
		return new ResponseEntity<>(myEmployees, HttpStatus.ACCEPTED);
	
	}
}
