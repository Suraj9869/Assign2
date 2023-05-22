package com.hrm.assign.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DecryptJwtToken {

	@Autowired
	JwtUtil jwtUtil;
	
	/**
	 * 
	 * @param 
	 * @return String @description username present in the JWT token
	 */
	public String decryptEmailId(HttpServletRequest request) {
		
		String commonToken = request.getHeader("Authorization").substring(7);
		return jwtUtil.extractUsername(commonToken);
		
	}
}
