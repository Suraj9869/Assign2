package com.hrm.assign.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.hrm.assign.dao.ManagerDAO;
import com.hrm.assign.dto.EmployeeDto;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.enums.Gender;
import com.hrm.assign.exception.ManagerException;
import com.hrm.assign.serviceImpl.IManagerService;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

	@InjectMocks
	IManagerService managerService;
	
	@Mock
	ManagerDAO managerDAO;

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
		
		String emailId = manager.getEmailId();
		
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.of(manager));
		
		Manager actual = managerService.getMyDetails(emailId);
		
		assertEquals(manager, actual);
		
	}
	
	@Test
	void getMyDetialsErrorTest() {
		
		String emailId = manager.getEmailId();
		
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.empty());
		
		ManagerException thrown = assertThrows(ManagerException.class, () -> managerService.getMyDetails(emailId), "Expected doThing() to throw, but it didn't");
		
		String expectedMessage = "Manager not found in the database";
		String actualMessage = thrown.getMessage();
		
		assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	void signUpTest() {
		
		Mockito.when(managerDAO.save(manager)).thenReturn(manager);
		
		Manager actual = managerService.signUp(manager);
		
		assertEquals(manager, actual);
		
	}
	
	@Test
	void listMyEmployeesTest() {
		
		String emailId = manager.getEmailId();
		
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.of(manager));
		
		List<Employee> actual = managerService.listMyEmployees(emailId);
		
		assertEquals(allEmployees, actual);
		
	}
	
	@Test
	void updateDetialsTest() throws ParseException {
		
		String emailId = manager.getEmailId();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		manager.setFromDate(sdf.parse("2024-05-09"));
		
		Mockito.when(managerDAO.findByEmailId(emailId)).thenReturn(Optional.of(manager));
		Mockito.when(managerDAO.save(manager)).thenReturn(manager);
		
		Manager actual = managerService.updateDetails(manager);
		
		assertEquals(manager, actual);
		
		
	}
}

