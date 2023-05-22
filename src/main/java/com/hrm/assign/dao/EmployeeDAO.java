package com.hrm.assign.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.assign.entity.Employee;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Integer>{

	public Optional<Employee> findByEmailId(String emailId);
	
	public void deleteByEmailId(String emailId);
	
}
