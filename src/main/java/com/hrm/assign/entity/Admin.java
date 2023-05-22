package com.hrm.assign.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer adminId;
	
	private String firstName;
	
	private String lastName;
	
	@NotBlank
	@Pattern(regexp = "[a-zA-Z0-9]+@anzz\\.com", message = "Please enter correct emailId")
	@Column(unique = true)
	private String userName;
	
	private String password;
	
	
	
}
