package com.hrm.assign.serviceImpl;

import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.exception.EmployeeException;
import com.hrm.assign.exception.ManagerException;
import com.hrm.assign.service.EmployeeService;
import com.hrm.assign.service.ManagerService;

@Service
public class IEmployeeService implements EmployeeService{

	@Autowired
	private EmployeeDAO employeeDAO;

	@Autowired
	private ManagerService managerService;
	
	Logger log = LoggerFactory.getLogger(IEmployeeService.class);
	
	/**
	 * 
	 * @param emailId @type String @decription Email id of the Employee
	 * @return Employee details
	 */
	public Employee getEmployeeDetials(String emailId) {
		
		Optional<Employee> employeeDtailsOpt = employeeDAO.findByEmailId(emailId);
		
		if(employeeDtailsOpt.isEmpty()) {
			log.debug("Employee emailId was not found in database {}", emailId);
			throw new EmployeeException("Employee not found in the database");
		}
		
		log.debug("Employee detials fetched {}", employeeDtailsOpt.get());
		return employeeDtailsOpt.get();
		
	}
	
	/**
	 * @param emailId @type String @description EmailId of the Employee
	 * @return Employee Dto
	 */
	@Override
	public Employee getMyDetails(String emailId) {
		
		Employee employeeDetails = getEmployeeDetials(emailId);
		log.debug("Fetched Employee detials {}", employeeDetails);
		
		return employeeDetails;
	}
	
	/**
	 * @param employee @type Employee
	 * @return Registered employee details
	 * @exception Employee Exception
	 */
	@Override
	public EmployeeDto registerEmployee(Employee employee) {
		
		Optional<Employee> employeeExists = employeeDAO.findByEmailId(employee.getEmailId());
		
		if(employeeExists.isPresent()) {
			log.debug("Trying to register the same employee again {}", employee);
			throw new EmployeeException("Employee is already registered");
		}

		Manager manager = employee.getManagerReports();
		if(manager != null && manager.getEmailId() != null) {
			
			manager = managerService.getMyDetails(manager.getEmailId());
			employee.setManagerReports(manager);
		}
		
		Employee employeeDtails = employeeDAO.save(employee);
		
		EmployeeDto employeeDto = new EmployeeDto();
		
		employeeDto.setEmailId(employeeDtails.getEmailId());
		employeeDto.setFirstName(employee.getFirstName());
		employeeDto.setLastName(employeeDtails.getLastName());
		employeeDto.setBirthDate(employee.getBirthDate());
		employeeDto.setPhoneNumber(employee.getPhoneNumber());
		employeeDto.setAddress(employee.getAddress());
		employeeDto.setGender(employee.getGender());
		
		return employeeDto;
		
	}

	/**
	 * @param employee @type Employee 
	 * @return Updated Employee details
	 */
	@Override
	public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
		
		String emailId = employeeDto.getEmailId();
		
		Employee prevEmployeeDetail =  getEmployeeDetials(emailId);
		
		prevEmployeeDetail.setFirstName(employeeDto.getFirstName());
		prevEmployeeDetail.setLastName(employeeDto.getLastName());
		prevEmployeeDetail.setGender(employeeDto.getGender());
		prevEmployeeDetail.setBirthDate(employeeDto.getBirthDate());
		prevEmployeeDetail.setAddress(employeeDto.getAddress());
		prevEmployeeDetail.setPhoneNumber(employeeDto.getPhoneNumber());
		
		Employee currentEmployee = employeeDAO.save(prevEmployeeDetail);
		
		employeeDto.setEmailId(currentEmployee.getEmailId());
		employeeDto.setFirstName(currentEmployee.getFirstName());
		employeeDto.setLastName(currentEmployee.getLastName());
		employeeDto.setBirthDate(currentEmployee.getBirthDate());
		employeeDto.setPhoneNumber(currentEmployee.getPhoneNumber());
		employeeDto.setAddress(currentEmployee.getAddress());
		employeeDto.setGender(currentEmployee.getGender());

		return employeeDto;
	}

	/**
	 * @param emailId @type String 
	 * @return true if employee is deleted successfully
	 */
	@Override
	public boolean deleteEmployee(String emailId) {
		
		try {
			employeeDAO.deleteByEmailId(emailId);
		}
		catch (Exception e) {
			log.debug("Unable to delete an employee {}", emailId);
			throw new EmployeeException("Unable to delete an Employee data");
		}
		
		log.debug("Deleted employee having emailId {}", emailId);
		return true;
		
	}
	
	

	

}
