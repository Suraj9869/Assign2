package com.hrm.assign.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hrm.assign.dao.AuthRecordDAO;
import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dao.ManagerDAO;
import com.hrm.assign.dto.AdminRequest;
import com.hrm.assign.dto.JwtRequest;
import com.hrm.assign.entity.AuthRecord;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.exception.InvalidFieldException;
import com.hrm.assign.helper.JwtUtil;
import com.hrm.assign.helper.SendEmailHelper;
import com.hrm.assign.serviceImpl.CustomUserDetailService;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

	@InjectMocks
	AuthenticationController authenticationController;
	
	@Mock
	AuthenticationManager authenticationManager;
	
	@Mock
	CustomUserDetailService cusDetailService;
	
	@Mock
	AuthRecordDAO authRecordDAO;
	
	@Mock
	EmployeeDAO employeeDAO;
	
	@Mock
	ManagerDAO managerDAO;
	
	@Mock
	SendEmailHelper sendEmailHelper;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Mock
	JwtUtil jwtUtil;
	
	static JwtRequest jwtRequest;
	static AuthRecord authRecord;
	static AuthRecord authRecord1;
	static AdminRequest adminRequest;
	static Employee employee;
	
	@BeforeAll
	static void initalizeVariable() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		jwtRequest = new JwtRequest();
		
		jwtRequest.setUsername("admin@anzz.com");
		jwtRequest.setOtp("488583");
		
		authRecord = new AuthRecord();
		authRecord.setEmailId(jwtRequest.getUsername());
		authRecord.setOtp(jwtRequest.getOtp());
		authRecord.setCreatedOn(LocalDateTime.now());
		
		adminRequest = new AdminRequest();
		adminRequest.setUsername("admin@anzz.com");
		adminRequest.setPassword("admin@123");
		
		authRecord1 = new AuthRecord();
		authRecord1.setEmailId(adminRequest.getUsername());
		authRecord1.setOtp(adminRequest.getPassword());
		authRecord1.setCreatedOn(LocalDateTime.now());
		
		employee = new Employee();
		employee.setEmployeeId(1);
		employee.setEmailId("pankaj@gmail.com");
		employee.setFirstName("Pankaj");
		employee.setLastName("Patil");
		employee.setGender(Gender.Male);
		employee.setBirthDate(sdf.parse("2001-09-05"));
		employee.setAddress("Pune Maharashtra");
		employee.setPhoneNumber("9885385868");
		employee.setHireDate(sdf.parse("2022-08-08"));
		
	}
	
	@Test
	void generateTokenTest()  {
		
		User userDetials = new User(jwtRequest.getUsername(), jwtRequest.getOtp(), new ArrayList<>());
		String token = "fmfjfnnfjkdokwodowdfpfplforijrgingingingrnginht";
		Mockito.when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getOtp())))
				.thenReturn(new Authentication() {
					
					@Override
					public String getName() {
						// TODO Auto-generated method stub
						return jwtRequest.getUsername();
					}
					
					@Override
					public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public boolean isAuthenticated() {
						// TODO Auto-generated method stub
						return true;
					}
					
					@Override
					public Object getPrincipal() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object getDetails() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object getCredentials() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Collection<? extends GrantedAuthority> getAuthorities() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		
		Mockito.when(cusDetailService.loadUserByUsername(jwtRequest.getUsername())).thenReturn(userDetials);
		Mockito.when(jwtUtil.generateToken(userDetials)).thenReturn(token);
		Mockito.when(authRecordDAO.findByEmailId(jwtRequest.getUsername())).thenReturn(Optional.of(authRecord));
		
		ResponseEntity<Map<String, String>> actual = null;
		try {
			actual = authenticationController.generateToken(jwtRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(token, actual.getBody().get("token"));
		
	}
	
	@Test
	void generateAdminTokenTest() {
		
		User userDetials = new User(adminRequest.getUsername(), adminRequest.getPassword(), new ArrayList<>());
		String token = "fmfjfnnfjkdokwodowdfpfplforijrgingingingrnginht";
		Mockito.when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(adminRequest.getUsername(), adminRequest.getPassword())))
				.thenReturn(new Authentication() {
					
					@Override
					public String getName() {
						// TODO Auto-generated method stub
						return adminRequest.getUsername();
					}
					
					@Override
					public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public boolean isAuthenticated() {
						// TODO Auto-generated method stub
						return true;
					}
					
					@Override
					public Object getPrincipal() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object getDetails() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object getCredentials() {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Collection<? extends GrantedAuthority> getAuthorities() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		
		Mockito.when(cusDetailService.loadUserByUsername(adminRequest.getUsername())).thenReturn(userDetials);
		Mockito.when(jwtUtil.generateToken(userDetials)).thenReturn(token);
		
		ResponseEntity<Map<String, String>> actual = null;
		try {
			actual = authenticationController.generateAdminToken(adminRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(token, actual.getBody().get("token"));
	}
	
	@Test
	void sendOtpMailTest() {
		
		String emailId = "pankaj@anzz.com";
		String otp = "86838385";
		String employeeName = employee.getFirstName();
		
		
		Map<String, String> request = new HashMap<>();
		request.put("emailId", emailId);
		
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.of(employee));
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		Mockito.when(sendEmailHelper.generateOtp()).thenReturn(otp);
		Mockito.when(authRecordDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		Mockito.when(bCryptPasswordEncoder.encode(otp)).thenReturn("nfjvnnvfjnnefnjenvjnejnenjnvjn");
		Mockito.when(sendEmailHelper.sendOtpEmail(emailId, employeeName, otp)).thenReturn(true);
		
		ResponseEntity<Map<String, String>> response;
		try {
			response = authenticationController.sendOtpMail(request);
		} catch (InvalidFieldException e) {
			assertFalse(true);
			return;
		}
		
		String expected = "Otp has been sent successfully";
		String actual = response.getBody().get("msg");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expected, actual);
		
	}
	
	@Test
	void sendOtpMailErrorTest() {
		
		String emailId = "pankaj@anzz.com";
		String otp = "86838385";
		String employeeName = employee.getFirstName();
		
		
		Map<String, String> request = new HashMap<>();
		request.put("emailId", emailId);
		
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.of(employee));
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		Mockito.when(sendEmailHelper.generateOtp()).thenReturn(otp);
		Mockito.when(authRecordDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		Mockito.when(bCryptPasswordEncoder.encode(otp)).thenReturn("nfjvnnvfjnnefnjenvjnejnenjnvjn");
		Mockito.when(sendEmailHelper.sendOtpEmail(emailId, employeeName, otp)).thenReturn(false);
		
		ResponseEntity<Map<String, String>> response;
		try {
			response = authenticationController.sendOtpMail(request);
		} catch (InvalidFieldException e) {
			assertFalse(true);
			return;
		}
		
		String expected = "Unable to send a OTP";
		String actual = response.getBody().get("error");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(expected, actual);
		
	}
	
	
}
