package com.hrm.assign.service;

import java.util.Optional;


import org.springframework.stereotype.Service;

import com.hrm.assign.dto.AdminDto;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.entity.Employee;

@Service
public interface AdminService {
	
	public Optional<Admin> getAdminDetails(String emailId);
	
	public Admin registerAdmin(Admin admin);
	
	public AdminDto updateAdmin(Admin admin);
	
	public Employee getEmployeeDetails(String emailId);
	
	public Employee updateEmployee(Employee employee);
}
