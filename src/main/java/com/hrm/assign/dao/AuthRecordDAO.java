package com.hrm.assign.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hrm.assign.entity.AuthRecord;

public interface AuthRecordDAO extends JpaRepository<AuthRecord, Integer>{

	public Optional<AuthRecord> findByEmailId(String emailId);
	
}
