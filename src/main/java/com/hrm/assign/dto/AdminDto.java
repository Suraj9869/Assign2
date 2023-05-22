package com.hrm.assign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

	private String firstName;
	
	private String lastName;
	
	private String userName;

	@Override
	public String toString() {
		return "AdminDto [firstName=" + firstName + ", lastName=" + lastName + ", userName=" + userName + "]";
	}
	
}
