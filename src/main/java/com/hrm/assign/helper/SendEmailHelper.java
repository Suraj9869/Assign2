package com.hrm.assign.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.hrm.assign.serviceImpl.EmailSenderService;

@Component
public class SendEmailHelper {

	@Autowired
	EmailSenderService emailSenderService;
	
	Logger log = LoggerFactory.getLogger(SendEmailHelper.class);
	
	static final int LENGHT = 8;
	
	public String readFile(String path) throws IOException{
		
		Resource resource = new ClassPathResource(path);
		
		File file = resource.getFile();
		
		try(BufferedReader  bufferedReader = new BufferedReader(new FileReader(file))) {
			
			return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
		
		}
		catch (Exception e) {
			log.error("Unable to read a file");
			return null;
		}
		
	}

	public boolean sendOtpEmail(String to, String employeeName, String otp) {
		
		boolean status;
		String htmlContent;
		
		try {
			
			htmlContent = readFile("templates/SendOtp.txt");
			htmlContent = htmlContent.replace("user_name", employeeName)
									.replace("my_email_otp", otp);
			
			log.info("Readed a OTP html content from a file");
		} catch (IOException e) {
			
			log.info("Unable to read a file");
			return false;
		}
		
		status = emailSenderService.sendEmail(to, "PingId Generated", htmlContent);
		
		return status;
		
		
	}
	
	public boolean sendAdminCredentials(String to, String adminName, String username, String password) {
		
		boolean status;
		String htmlContent;
		
		try {
			htmlContent = readFile("templates/AdminEmail.txt");
			htmlContent = htmlContent.replace("admin_name", adminName)
									  .replace("admin_username", username)
									  .replace("admin_password", password);
			
			log.info("Readed Admin Credentials html content from a file");
		} catch (IOException e) {
			
			return false;
		}
		
		status = emailSenderService.sendEmail(to, "Admin HRM Credntials", htmlContent);
		
		return status;
		
		
	}
	
	public String generateOtp() {

		SecureRandom randomizer = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < LENGHT; i++) {
		   builder.append(randomizer.nextInt(10));
		}

		return builder.toString();
		
	}
	
	public char getAnyCharacter(int choice) {
		
		char ch;
		SecureRandom randomizer = new SecureRandom();
		String specialCharacter = "!@$%&";
		switch (choice) {
		case 0: {
			//Lowercase
			ch = (char) randomizer.nextInt(97, 123);
			break;
		}
		case 1: {
			//Uppercase
			ch = (char) randomizer.nextInt(65, 91);
			break;
		}
		case 2:{
			ch = specialCharacter.charAt(randomizer.nextInt(specialCharacter.length()));
			break;
		}
		case 3:{
			ch = (char) randomizer.nextInt(48, 58);
			break;
		}
		default:
			ch = '@';
			log.error("Unexpected value: {}", choice);
		}
		
		return ch;
		
		
	}
	public String generatePassword() {
		
		StringBuilder password = new StringBuilder();
		SecureRandom randomizer = new SecureRandom();
		
		for(int i = 0; i < LENGHT; i++) {
			password.append(getAnyCharacter(randomizer.nextInt(4)));
		}
		
		return password.toString();
		
	}
}
