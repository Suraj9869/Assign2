package com.hrm.assign.serviceImpl;

import java.util.ArrayList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hrm.assign.dao.AdminDAO;
import com.hrm.assign.dao.AuthRecordDAO;
import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dao.ManagerDAO;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.entity.AuthRecord;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Autowired
	private ManagerDAO managerDAO;
	
	@Autowired
	private AdminDAO adminDAO;
	
	@Autowired
	private AuthRecordDAO authRecordDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ArrayList<GrantedAuthority> roles = new ArrayList<>();
		
		Optional<Admin> adminOpt = adminDAO.findByUserName(username);
		
		if(adminOpt.isPresent()) {
			
			Admin admin = adminOpt.get();
			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User(admin.getUserName(), admin.getPassword(), roles);
			
		}
		
		Optional<AuthRecord> authDataOpt = authRecordDAO.findByEmailId(username);
		
		if(authDataOpt.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found");
		}
		
		AuthRecord authData = authDataOpt.get();
		
		Optional<Employee> employeeOpt= employeeDAO.findByEmailId(username);
		Optional<Manager> managerOpt = managerDAO.findByEmailId(username);
		

		if(employeeOpt.isPresent()) {
			
			roles.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
		}
		
		if(managerOpt.isPresent()){
				
				roles.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		}
		
		return new User(authData.getEmailId(), authData.getOtp(), roles);
	}
}
