package com.hrm.assign.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;

@Service
public interface ManagerService {

	public Manager getMyDetails(String emailId);
	
	public Manager signUp(Manager manager);
	
	public List<Employee> listMyEmployees(String emailId);
	
	public Manager updateDetails(Manager manager);
}
