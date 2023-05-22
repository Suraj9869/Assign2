package com.hrm.assign.controller;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hrm.assign.dao.AuthRecordDAO;
import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dao.ManagerDAO;
import com.hrm.assign.dto.AdminRequest;
import com.hrm.assign.dto.JwtRequest;
import com.hrm.assign.entity.AuthRecord;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.exception.AuthRecordException;
import com.hrm.assign.exception.InvalidFieldException;
import com.hrm.assign.exception.TokenValidationException;
import com.hrm.assign.helper.JwtUtil;
import com.hrm.assign.helper.SendEmailHelper;
import com.hrm.assign.serviceImpl.CustomUserDetailService;


@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CustomUserDetailService customUserDetailService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	SendEmailHelper sendEmailHelper;
	
	@Autowired
	AuthRecordDAO authRecordDAO;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	EmployeeDAO employeeDAO;
	
	@Autowired
	ManagerDAO managerDAO;

	Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	/**
	 * Generate token for the Employee or Manager
	 * 
	 * @param jwtRequest @type JwtRequest @description Contains username and otp
	 * @return Generates the token and returns in the response body
	 * @throws Exception This exception is related to BadCredentials or the UsernameNotFoundException
	 */
	@PostMapping("/token")
	public ResponseEntity<Map<String, String>> generateToken(@RequestBody JwtRequest jwtRequest){
		
		try {
			
			log.info("User requested for the token.");
			
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(), jwtRequest.getOtp()));
			
			log.info("Used detils validated");
		}catch (UsernameNotFoundException e) {
			
			throw new TokenValidationException("Usename Not Found");
		}
		catch (BadCredentialsException e) {
			
			throw new TokenValidationException("Credentials are not correct");
		}
		catch (Exception e) {
			throw new TokenValidationException(e.getMessage());
		}
		
		UserDetails userDetails = customUserDetailService.loadUserByUsername(jwtRequest.getUsername());
		String token = this.jwtUtil.generateToken(userDetails);
		
		log.info("Generated token for the user");
		
		//Update jwt token in auth Record
		Optional<AuthRecord> authOpt = authRecordDAO.findByEmailId(jwtRequest.getUsername());
		
		if(authOpt.isPresent()) {
			AuthRecord authRec = authOpt.get();
			authRec.setJwtToken(token);
			
			try {
				authRecordDAO.save(authRec);
			}catch (Exception e) {
				log.debug("Auth Record not saved {}", authRec);
				throw new AuthRecordException("Unable to save the AuthRecord Data in database");
			}
			
			log.info("AuthRecord updated for the user {}", authRec);
		}
		
		Map<String, String> jwtResponse = new HashMap<>();
		jwtResponse.put("token", token);
		
		return ResponseEntity.ok(jwtResponse);
		
	}
	
	/**
	 * Authenticates and generates token if all credentials are correct for Admin
	 * 
	 * @param adminRequest @type AdminRequest @description Contains username and the password
	 * @return 
	 * @throws Exception This exception is related to BadCredentials or the UsernameNotFoundException
	 */
	@PostMapping("/adminToken")
	public ResponseEntity<Map<String, String>> generateAdminToken(@RequestBody AdminRequest adminRequest) throws Exception{
		
		try {
			
			log.info("Admin requested for the token");
			
			this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							adminRequest.getUsername(), adminRequest.getPassword()));
			
		}catch (UsernameNotFoundException e) {
			
			log.debug("Admin emailId is not present in DB {}", adminRequest.getUsername());
			throw new TokenValidationException("Usename not found in DB");
		}
		catch (BadCredentialsException e) {

			log.debug("Credentials is not correct {}", adminRequest);
			throw new TokenValidationException("Admin credentials is not correct");
		}
		catch (Exception e) {
			
			log.debug("Credentials is not correct {}", adminRequest);
			throw new TokenValidationException(e.getMessage());
		}
		
		UserDetails userDetails = customUserDetailService.loadUserByUsername(adminRequest.getUsername());
		String token = this.jwtUtil.generateToken(userDetails);
		
		log.info("Admin token generated.");
		
		Map<String, String> jwtResponse = new HashMap<>();
		jwtResponse.put("token", token);
		
		return ResponseEntity.ok(jwtResponse);
		
	}
	
	/**
	 * Sent OTP to the Mail for Employee and Manager
	 * 
	 * @param request Contains the emailId of the user where to send an OTP
	 * @return Success or failed response once the OTP is send to the email
	 * @throws InvalidFieldException 
	 */
	@PostMapping("/sendOtpMail")
	public ResponseEntity<Map<String, String>> sendOtpMail(@RequestBody Map<String, String> request) throws InvalidFieldException{
		
		log.info("Requested for the OTP");
		
		if(!request.containsKey("emailId")) {
			throw new InvalidFieldException("EmailId field is not present");
		}
		
		String emailId = request.get("emailId");
		Optional<Employee> employeeOpt= employeeDAO.findByEmailId(emailId);
		Optional<Manager> managerOpt = managerDAO.findByEmailId(emailId);
		
		Map<String, String> response = new HashMap<>();
		
		if(employeeOpt.isEmpty() && managerOpt.isEmpty()) {
			
			log.debug("EmailId provided is not present in the database {}", emailId);
			response.put("error", "Invalid emailId. Please enter correct emailId");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		String employeeName = employeeOpt.isPresent() ? employeeOpt.get().getFirstName() 
														: managerOpt.get().getEmailId().split("@")[0];
		
		String otp = sendEmailHelper.generateOtp();
		log.info("OTP generated");
		
		Optional<AuthRecord> authOpt = authRecordDAO.findByEmailId(emailId);
		AuthRecord auth = new AuthRecord();
		
		//If user requested for OTP again
		if(authOpt.isPresent()) {
			auth = authOpt.get();
		}
		else {
			auth.setEmailId(emailId);
		}
		
		auth.setOtp(bCryptPasswordEncoder.encode(otp));
		auth.setCreatedOn(LocalDateTime.now());
		
		boolean sentStatus = sendEmailHelper.sendOtpEmail(emailId, employeeName, otp);
		
		
		if(sentStatus) {
			log.info("OTP sent successfully to the emailId");

			try {
				authRecordDAO.save(auth);
			}
			catch (Exception e) {
				log.debug("Auth Record not saved {}", auth);
				throw new AuthRecordException("Unable to save the AuthRecord Data in database");
			}
			
			response.put("msg", "Otp has been sent successfully");
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		else {
			log.info("Unable to send an OTP to the emailId");
			response.put("error", "Unable to send a OTP");
			
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		
		}
		
		
	}
	
}
