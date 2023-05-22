package com.hrm.assign.serviceImpl;

import java.util.Optional;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hrm.assign.dao.AdminDAO;
import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dto.AdminDto;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.exception.AdminException;
import com.hrm.assign.exception.EmployeeException;
import com.hrm.assign.service.AdminService;
import com.hrm.assign.service.ManagerService;

@Service
public class IAdminService implements AdminService{

	@Autowired
	private AdminDAO adminDAO;
	
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ManagerService managerService;
	
	Logger log = LoggerFactory.getLogger(IAdminService.class);
	
	/**
	 * @author surajrya
	 * @param admin @type Admin @description Admin details to be saved
	 * @return Admin details once the admin is registered in to the database
	 * @exception AdminException it is raised if the admin username is already present
	 */
	@Override
	public Admin registerAdmin(Admin admin) {

		log.debug("Admin Details achieved");
		
		Optional<Admin> adminOpt = getAdminDetails(admin.getUserName());
		
		if(adminOpt.isPresent()) {
			log.debug("Admin username is already present {}", admin);
			throw new AdminException("Admin username is already registered ".concat(admin.getUserName()));
		}
		
		return adminDAO.save(admin);
		
	}

	/**
	 * 
	 * @param emailId @type String @description EmailId
	 * @return Admin details fetched by user
	 */
	@Override
	public Optional<Admin> getAdminDetails(String emailId) {
		
		return adminDAO.findByUserName(emailId);
	}

	/**
	 * 
	 * @param admin @type Admin @description Admin details 
	 * @return The updated details of the admin
	 * 
	 */
	@Override
	public AdminDto updateAdmin(Admin admin) {
		
		Admin adminDetails = getAdminDetails(admin.getUserName()).get();
		
		adminDetails.setFirstName(admin.getFirstName());
		adminDetails.setLastName(admin.getLastName());
		adminDetails.setPassword(passwordEncoder.encode(admin.getPassword()));
		
		Admin adminData = adminDAO.save(adminDetails);
		
		log.debug("Admin detials updated {}", adminData);
		
		return new AdminDto(admin.getFirstName(), admin.getLastName(), admin.getUserName());
	}
	
	/**
	 * 
	 * @param emailId @type String @description Email id of the Employee
	 * @return Employee details
	 */
	@Override
	public Employee getEmployeeDetails(String emailId) {
		
		Optional<Employee> employeeDtailsOpt = employeeDAO.findByEmailId(emailId);
		
		if(employeeDtailsOpt.isEmpty()) {
			log.debug("Employee emailId was not found in database {}", emailId);
			throw new EmployeeException("Employee not found in the database");
		}
		
		log.debug("Employee detials fetched {}", employeeDtailsOpt.get());
		return employeeDtailsOpt.get();
		
	}
	
	@Override
	public Employee updateEmployee(Employee employee) {
		
		getEmployeeDetails(employee.getEmailId());
		
		Manager manager = employee.getManagerReports();
		if(manager != null && manager.getEmailId() != null) {
			
			manager = managerService.getMyDetails(manager.getEmailId());
			employee.setManagerReports(manager);
		}
		
		return employeeDAO.save(employee);
		
		
	}
	
	

}
