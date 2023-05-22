package com.hrm.assign.dao;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.assign.entity.Admin;

@Repository
public interface AdminDAO extends JpaRepository<Admin, Integer>{

	Optional<Admin> findByUserName(String username);
	
}
