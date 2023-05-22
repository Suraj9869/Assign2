package com.hrm.assign.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
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
import com.hrm.assign.service.EmployeeService;


@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

	@InjectMocks
	EmployeeController employeeController;
	
	@Mock
	EmployeeService employeeService;
	
	@Mock
	DecryptJwtToken decryptJwtToken;
	
	static Employee employee;
	static Manager manager;
	static EmployeeDto employeeDto;
	
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
		employee.setBirthDate(sdf.parse("2001-05-09"));
		employee.setAddress("Pune Maharashtra");
		employee.setPhoneNumber("9885385868");
		employee.setHireDate(sdf.parse("2022-08-08"));
		employee.setManagerReports(manager);
		
		
		List<Employee> allEmployees = Arrays.asList(employee);
		manager.setEmployeesList(allEmployees);
		
		employeeDto = new EmployeeDto();
		employeeDto.setEmailId(employee.getEmailId());
		employeeDto.setFirstName(employee.getFirstName());
		employeeDto.setLastName(employee.getLastName());
		employeeDto.setBirthDate(employee.getBirthDate());
		employeeDto.setPhoneNumber(employee.getPhoneNumber());
		employeeDto.setAddress(employee.getAddress());
		employeeDto.setGender(Gender.Male);
		
	}
	
	@Test
	void getMyDetailsTest() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		String emailId = employee.getEmailId();
		
		
		Mockito.when(decryptJwtToken.decryptEmailId(request)).thenReturn(emailId);
		Mockito.when(employeeService.getMyDetails(emailId)).thenReturn(employee);
		
		ResponseEntity<Employee> actual = employeeController.getMyDetails(request);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employee, actual.getBody());
		
		
	}
	
	@Test
	void signUpEmployeeTest() {
		
		Mockito.when(employeeService.registerEmployee(employee)).thenReturn(employeeDto);
		
		ResponseEntity<EmployeeDto> actual = employeeController.signUpEmployee(employee);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employeeDto, actual.getBody());
		
	}
	
	@Test
	void updateEmployeeTest() {
		
		Mockito.when(employeeService.updateEmployee(employeeDto)).thenReturn(employeeDto);
		
		ResponseEntity<EmployeeDto> actual = employeeController.updateEmployee(employeeDto);
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(employeeDto, actual.getBody());
		
	}
	
	@Test
	void deleteEmployeeTest() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		String emailId = "pankaj@gmail.com";
		
		
		Mockito.when(decryptJwtToken.decryptEmailId(request)).thenReturn(emailId);
		Mockito.when(employeeService.deleteEmployee(emailId)).thenReturn(true);
		
		ResponseEntity<String> actual  = employeeController.deleteEmployee(request);
		String expected = "Employee Successfully Deleted";
		
		assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
		assertEquals(expected, actual.getBody());
		
	}
	
	@Test
	void deleteEmployeeErrorTest() {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		String emailId = employee.getEmailId();
		
		
		Mockito.when(decryptJwtToken.decryptEmailId(request)).thenReturn(emailId);
		Mockito.when(employeeService.deleteEmployee(emailId)).thenReturn(false);
		
		ResponseEntity<String> actual  = employeeController.deleteEmployee(request);
		String expected = "Unable to delete Employee";
		
		assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
		assertEquals(expected, actual.getBody());
		
	}
	
	@AfterAll
	static void deinitalizeVariable() {
		
		employee = null;
		manager = null;
		employeeDto = null;
	
	}
	
	
	
}
