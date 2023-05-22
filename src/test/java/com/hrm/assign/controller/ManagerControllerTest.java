package com.hrm.assign.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.helper.DecryptJwtToken;
import com.hrm.assign.service.ManagerService;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {
	
	@InjectMocks
	ManagerController managerController;
	
	@Mock
	ManagerService managerService;
	
	@Mock
	DecryptJwtToken decryptJwtToken;

	static Employee employee;
	static Manager manager;
	static EmployeeDto employeeDto;
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
	}
	
	@Test
	void getMyDetialsTest() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		String emailId = manager.getEmailId();
		
		Mockito.when(decryptJwtToken.decryptEmailId(request)).thenReturn(emailId);
		Mockito.when(managerService.getMyDetails(emailId)).thenReturn(manager);
		
		ResponseEntity<Manager> actual = managerController.getMyDetails(request);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(manager, actual.getBody());
		
		
	}
	
	@Test
	void signUpManagerTest() {
		
		Mockito.when(managerService.signUp(manager)).thenReturn(manager);
		
		ResponseEntity<Manager> actual = managerController.signUpManager(manager);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(manager, actual.getBody());
		
	}
	
	@Test
	void updateMyDetailsTest() throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		manager.setToDate(sdf.parse("2024-05-09"));
		Mockito.when(managerService.updateDetails(manager)).thenReturn(manager);
		
		ResponseEntity<Manager> actual = managerController.updateMyDetails(manager);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(manager, actual.getBody());
	}
	
	
	@Test
	void getMyEmployeesTest() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		Mockito.when(decryptJwtToken.decryptEmailId(request)).thenReturn(manager.getEmailId());
		Mockito.when(managerService.listMyEmployees(manager.getEmailId())).thenReturn(allEmployees);
		
		ResponseEntity<List<Employee>> actual = managerController.getMyEmployees(request);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(allEmployees, actual.getBody());
	}
	
}
