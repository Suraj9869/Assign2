package com.hrm.assign.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AdminDtoTest {
	
	private AdminDto adminDto;
	
	@Test
	void testNoArgConstuctor() {
		
		adminDto = new AdminDto();
		
		String firstName = "Pankaj";
		String lastName = "Patil";
		String userName = "Pankaj@123";
		
		
		adminDto.setFirstName(firstName);
		adminDto.setLastName(lastName);
		adminDto.setUserName(userName);
		
		assertEquals(firstName, adminDto.getFirstName());
		assertEquals(lastName, adminDto.getLastName());
		assertEquals(userName, adminDto.getUserName());
		
	}
	
	@Test
	void testAllArgConstructor() {
		
		String firstName = "Pankaj";
		String lastName = "Patil";
		String userName = "Pankaj@123";
		
		adminDto = new AdminDto(firstName, lastName, userName);
		
		assertEquals(firstName, adminDto.getFirstName());
		assertEquals(lastName, adminDto.getLastName());
		assertEquals(userName, adminDto.getUserName());
	}

}
