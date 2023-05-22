package com.hrm.assign.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.assign.entity.Manager;

@Repository
public interface ManagerDAO extends JpaRepository<Manager, Integer>{

	public Optional<Manager> findByEmailId(String emailId);
	
}

