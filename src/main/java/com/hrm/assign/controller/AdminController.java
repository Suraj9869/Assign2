package com.hrm.assign.controller;

import java.util.Optional;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.assign.dto.AdminDto;
import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.exception.AdminException;
import com.hrm.assign.helper.SendEmailHelper;
import com.hrm.assign.service.AdminService;
import com.hrm.assign.service.EmployeeService;
import com.hrm.assign.service.ManagerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SendEmailHelper sendEmailHelper;
	
	Logger log = LoggerFactory.getLogger(AdminController.class);
	
	/**
	 * Admin fetched Employee details
	 * 
	 * @param emailId @type String @description EmailId of the Employee
	 * @return Employee details
	 */
	@GetMapping("/getEmployeeDetails")
	public ResponseEntity<Employee> getEmployeeDetails(@RequestParam String emailId){
		
		Employee employeeDetails = adminService.getEmployeeDetails(emailId);
		
		log.info("Admin fetched the employee details");
		log.debug("Employee detials that admin has fetched is {} " , employeeDetails);
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
	}
	
	/**
	 * 
	 * @param employee @type Employee @description Employee details that needs to be added.
	 * @return Added employee details DTO
	 */
	@PostMapping("/addEmployee")
	public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody Employee employee) {
		
		EmployeeDto employeeDetails = employeeService.registerEmployee(employee);
		
		log.info("Admin added employee in to the database");
		log.debug("Admin created employee detials {}" ,employeeDetails);
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * @param employee @type Employee @description Employee details that needs to be updated
	 * @return Updated Employee details is returned
	 */
	@PutMapping("/updateEmployee")
	public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee){
		
		Employee employeeDetails = adminService.updateEmployee(employee);
		
		log.info("Admin updated employee details");
		log.debug("Updated employee detials: {}", employeeDetails);
		
		return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * @param manager @type Manager @description Manager details that needs to be added.
	 * @return Added manager details DTO
	 */
	@PostMapping("/addManager")
	public ResponseEntity<Manager> createManager(@Valid @RequestBody Manager manager){
		
		Manager managerDetails = managerService.signUp(manager);
		
		log.info("Admin got registered in to the database");
		return new ResponseEntity<>(managerDetails, HttpStatus.ACCEPTED);
		
	}
	
	
	/**
	 * Admin fetched Manager details
	 * 
	 * @param emailId @type String @description EmailId of the Manager
	 * @return Manager details
	 */
	@GetMapping("/getManagerDetails")
	public ResponseEntity<Manager> getManagerDetails(@RequestParam String emailId){
		
		Manager managerDetails = managerService.getMyDetails(emailId);
		
		log.info("Admin fetched the manager details");
		log.debug("Manager detials that admin has fetched is {} " , managerDetails);
		
		return new ResponseEntity<>(managerDetails, HttpStatus.ACCEPTED);
	}
	
	/**
	 * Update Admin Details
	 * 
	 * @param admin @type Admin @description Admin details that need to be updated
	 * @return Updated admin details DTO
	 */
	@PostMapping("/updateAdmin")
	public ResponseEntity<AdminDto> updateAdmin(@RequestBody Admin admin) {
		
		AdminDto adminDetails = adminService.updateAdmin(admin);
		
		log.info("Admin details updated successfully");
		log.debug("Written admin detials {}", adminDetails);
		
		return new ResponseEntity<>(adminDetails, HttpStatus.ACCEPTED);
		
	}
	
	/**
	 * 
	 * This function will be triggered after bean definitions are loaded
	 * This will create a default entry in admin Database related to credentials
	 * Then it will send that credentials and allow admin to change the password as soon as possible
	 * 
	 */
	@EventListener(ApplicationStartedEvent.class)
	public boolean runAfterStartup() {
	    
		String adminUsername = "admin@anzz.com";
		
		Optional<Admin> adminOpt = adminService.getAdminDetails(adminUsername);
		
		if(adminOpt.isPresent()) {
			return false;
		}
		
		String adminPassword = sendEmailHelper.generatePassword();
		
		log.info("Admin random password created");
		
		Admin admin = new Admin();
		admin.setFirstName("Shayne");
		admin.setLastName("Elliot");
		admin.setUserName(adminUsername);
		admin.setPassword(passwordEncoder.encode(adminPassword));
		
		boolean status = sendEmailHelper.sendAdminCredentials(admin.getUserName(), 
				admin.getFirstName(), admin.getUserName(), adminPassword);
		
		if(status) {
			try {
				adminService.registerAdmin(admin);
			}
			catch (Exception e) {
				
				log.debug("Admin detials not save to DB {}", admin);
				throw new AdminException("Unable to save the admin details");
			}
			
			log.info("Successfully created Admin Profile");
			return true;
		}
		else {
			log.info("Unable to send an email to an Admin");
			return false;
		}
		
	}
	
}
