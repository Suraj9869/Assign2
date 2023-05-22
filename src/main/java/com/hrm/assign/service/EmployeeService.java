package com.hrm.assign.service;

import org.springframework.stereotype.Service;

import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;

@Service
public interface EmployeeService {
	
	public Employee getMyDetails(String emailId);
	
	public EmployeeDto registerEmployee(Employee employee);
	
	public EmployeeDto updateEmployee(EmployeeDto employeeDto);
	
	public boolean deleteEmployee(String emailId);
	
}
