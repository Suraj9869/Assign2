package com.hrm.assign.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hrm.assign.dto.AdminDto;
import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Admin;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.helper.SendEmailHelper;
import com.hrm.assign.service.AdminService;
import com.hrm.assign.service.EmployeeService;
import com.hrm.assign.service.ManagerService;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

	@InjectMocks
	AdminController adminController;
	
	@Mock
	EmployeeService employeeService;
	
	@Mock
	ManagerService managerService;
	
	@Mock
	AdminService adminService;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	SendEmailHelper sendEmailHelper;
	
	static Employee employee;
	static Manager manager;
	static Admin admin;
	static EmployeeDto employeeDto;
	static AdminDto adminDto;
	static List<Employee> allEmployees;
	
	@BeforeAll
	static void createEmployee() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		manager = new Manager();
		manager.setManagerId(1);
		manager.setEmailId("shyam@gmail.com");
		manager.setFromDate(sdf.parse("2023-01-01"));
		manager.setToDate(sdf.parse("2024-01-01"));
		
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
		employee.setManagerReports(manager);
		
		
		allEmployees = Arrays.asList(employee);
		manager.setEmployeesList(allEmployees);
		
		employeeDto = new EmployeeDto();
		employeeDto.setEmailId(employee.getEmailId());
		employeeDto.setFirstName(employee.getFirstName());
		employeeDto.setLastName(employee.getLastName());
		employeeDto.setBirthDate(employee.getBirthDate());
		employeeDto.setPhoneNumber(employee.getPhoneNumber());
		employeeDto.setAddress(employee.getAddress());
		employeeDto.setGender(Gender.Male);
		
		admin = new Admin();
		admin.setAdminId(1);
		admin.setFirstName("Shayne");
		admin.setLastName("Shayne");
		admin.setUserName("admin@anzz.com");
		admin.setPassword("dkkkfkkckdjdvnvvjvjnnjvnjdvnjunvjnunojnaocjncjncaxslacplvojnegbv");
		
		adminDto = new AdminDto();
		adminDto.setFirstName(admin.getFirstName());
		adminDto.setLastName(admin.getLastName());
		adminDto.setUserName(admin.getUserName());
	}
	
	@Test
	void getEmployeeTest() {
		
		String emailId = employee.getEmailId();
		Mockito.when(adminService.getEmployeeDetails(emailId)).thenReturn(employee);
		
		ResponseEntity<Employee> actual = adminController.getEmployeeDetails(emailId);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employee, actual.getBody());
		
		
	}
	
	@Test
	void createEmployeeTest() {
		
		Mockito.when(employeeService.registerEmployee(employee)).thenReturn(employeeDto);
		
		ResponseEntity<EmployeeDto> actual = adminController.createEmployee(employee);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employeeDto, actual.getBody());
		
	}
	
	@Test
	void updateEmployeeTest() {
		
		Mockito.when(adminService.updateEmployee(employee)).thenReturn(employee);
		
		ResponseEntity<Employee> actual = adminController.updateEmployee(employee);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employee, actual.getBody());
		
	}
	
	@Test
	void addManagerTest() {
		
		Mockito.when(managerService.signUp(manager)).thenReturn(manager);
		
		ResponseEntity<Manager> actual = adminController.createManager(manager);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(manager, actual.getBody());
		
	}
	
	@Test
	void updateAdminTest() {
		
		admin.setFirstName("Suraj");
		adminDto.setFirstName(admin.getFirstName());
		
		Mockito.when(adminService.updateAdmin(admin)).thenReturn(adminDto);
		
		ResponseEntity<AdminDto> actual = adminController.updateAdmin(admin);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(adminDto, actual.getBody());
	}
	
	@Test
	void runAfterStartupTest() {
		
		String userName = admin.getUserName();
		String password = "gHgjG@jF";	
		
		Mockito.when(adminService.getAdminDetails(userName)).thenReturn(Optional.empty());
		Mockito.when(sendEmailHelper.generatePassword()).thenReturn(password);
		Mockito.when(sendEmailHelper.sendAdminCredentials(admin.getUserName(), admin.getFirstName(), 
				admin.getUserName(), password)).thenReturn(true);
		
		boolean actual = adminController.runAfterStartup();
		
		assertTrue(actual);
		
		
	}
	
	@Test
	void runAfterStartupErrorTest() {
		
		String userName = admin.getUserName();
		String password = "gHgjG@jF";
		
		Mockito.when(adminService.getAdminDetails(userName)).thenReturn(Optional.empty());
		Mockito.when(sendEmailHelper.generatePassword()).thenReturn(password);
		Mockito.when(sendEmailHelper.sendAdminCredentials(admin.getUserName(), admin.getFirstName(), 
				admin.getUserName(), password)).thenReturn(false);
		
		boolean actual = adminController.runAfterStartup();
		
		assertFalse(actual);
		
		
	}
	
	
	
}
