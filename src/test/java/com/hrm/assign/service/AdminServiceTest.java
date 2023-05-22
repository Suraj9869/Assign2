package com.hrm.assign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hrm.assign.dao.AdminDAO;
import com.hrm.assign.dto.AdminDto;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.exception.AdminException;
import com.hrm.assign.serviceImpl.IAdminService;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

	@InjectMocks
	IAdminService adminService;
	
	@Mock
	AdminDAO adminDAO;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	static Admin admin;
	static AdminDto adminDto;

	@BeforeAll
	static void initializeVariable() {
		
		admin = new Admin();
		admin.setAdminId(1);
		admin.setFirstName("Shayne");
		admin.setLastName("Ellite");
		admin.setUserName("admin@anzz.com");
		admin.setPassword("admin@123");
		
		adminDto = new AdminDto();
		adminDto.setFirstName(admin.getFirstName());
		adminDto.setLastName(admin.getLastName());
		adminDto.setUserName(admin.getUserName());
		
		
	}
	
	@Test
	void getAdminDetailsTest() {
		
		String userName = admin.getUserName();
		Mockito.when(adminDAO.findByUserName(userName)).thenReturn(Optional.of(admin));
		
		Optional<Admin> actual = adminService.getAdminDetails(userName);
		
		assertEquals(admin, actual.get());

	}
	
	@Test
	void registerAdminTest() {
		
		String userName = admin.getUserName();
		Mockito.when(adminDAO.findByUserName(userName)).thenReturn(Optional.empty());
		Mockito.when(adminDAO.save(admin)).thenReturn(admin);
		
		Admin actual = adminService.registerAdmin(admin);
		
		assertEquals(admin, actual);
		
		
	}
	
	@Test
	void registerAdminErrorTest() {
		
		String userName = admin.getUserName();
		Mockito.when(adminDAO.findByUserName(userName)).thenReturn(Optional.of(admin));
		
		AdminException thrown = assertThrows(AdminException.class, () -> adminService.registerAdmin(admin), "Expected some error in function but didnt raised");
			
		String expecMsg = "Admin username is already registered ".concat(admin.getUserName());
		String actualMsg = thrown.getMessage();
		
		assertEquals(expecMsg, actualMsg);
	}
	
	@Test
	void updateAdminTest() {
		
		admin.setLastName("ellite");
		String userName = admin.getUserName();
		Mockito.when(adminDAO.findByUserName(userName)).thenReturn(Optional.of(admin));
		Mockito.when(passwordEncoder.encode(admin.getPassword())).thenReturn("fnjffnfnjnjfmfkmsdmC<kmkmvlkmmkvzm");
		Mockito.when(adminDAO.save(admin)).thenReturn(admin);
		
		AdminDto adminDto = adminService.updateAdmin(admin);
		
		String expected = admin.getLastName();
		String actual = adminDto.getLastName();
		
		assertEquals(expected, actual);
	}
}
