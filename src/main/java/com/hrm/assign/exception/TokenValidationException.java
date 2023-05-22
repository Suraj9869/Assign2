package com.hrm.assign.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenValidationException extends RuntimeException {

	private static final long serialVersionUID = 6L;
	static final Logger log = LoggerFactory.getLogger(TokenValidationException.class);
	
	public TokenValidationException(String msg) {
		super(msg);
		log.error("TokenValidationException raised {}" , msg);
	}
	
	
	
}
