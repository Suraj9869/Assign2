package com.hrm.assign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hrm.assign.dao.EmployeeDAO;
import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.exception.EmployeeException;
import com.hrm.assign.serviceImpl.IEmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@InjectMocks
	IEmployeeService employeeService;
	
	@Mock
	EmployeeDAO employeeDAO;
	
	@Mock
	ManagerService managerService;
	
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
		employee.setBirthDate(sdf.parse("2001-09-05"));
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
	void getEmployeeDetialsTest() {
		
		String emailId = employee.getEmailId();
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.of(employee));
		
		Employee actual = employeeService.getEmployeeDetials(emailId);
		
		assertEquals(employee, actual);
		
	}
	
	@Test
	void getEmployeeDetialsErrorTest() {
		
		String emailId = employee.getEmailId();
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		
		EmployeeException thrown = assertThrows(EmployeeException.class, () -> employeeService.getEmployeeDetials(emailId), "Expected getEmployeeDetials() to throw, but it didn't");
		
		String expectedMessage = "Employee not found in the database";
		String actualMessage = thrown.getMessage();
		
		assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	void getMyDetailsTest() {
		
		String emailId = employee.getEmailId();
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.of(employee));
		
		Employee actual = employeeService.getMyDetails(emailId);
		
		assertEquals(employee.getEmailId(), actual.getEmailId());
		assertEquals(employee.getFirstName(), actual.getFirstName());
		assertEquals(employee.getLastName(), actual.getLastName());
		
	}
	
	@Test
	void registerEmployee() {
		
		String emailId = employee.getEmailId();
		
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		Mockito.when(managerService.getMyDetails(manager.getEmailId())).thenReturn(manager);
		Mockito.when(employeeDAO.save(employee)).thenReturn(employee);
		
		EmployeeDto actual = employeeService.registerEmployee(employee);
		
		assertEquals(employee.getEmailId(), actual.getEmailId());
		assertEquals(employee.getFirstName(), actual.getFirstName());
		assertEquals(employee.getLastName(), actual.getLastName());
		
		
	}
	
	@Test
	void updateEmployeeTest() {
		
		String emailId = employee.getEmailId();
		
		Mockito.when(employeeDAO.findByEmailId(emailId)).thenReturn(Optional.of(employee));
		Mockito.when(employeeDAO.save(employee)).thenReturn(employee);
		
		EmployeeDto actual = employeeService.updateEmployee(employeeDto);
		
		assertEquals(employee.getEmailId(), actual.getEmailId());
		assertEquals(employee.getFirstName(), actual.getFirstName());
		assertEquals(employee.getLastName(), actual.getLastName());
		
	}
	
	@Test
	void deleteEmployee() {
		
		String emailId = employee.getEmailId();
		
		boolean actual = employeeService.deleteEmployee(emailId);
		
		assertTrue(actual);
		
	}
	
	@AfterAll
	static void deinitalizeVariable() {
		
		employee = null;
		manager = null;
		employeeDto = null;
	
	}
	
}
