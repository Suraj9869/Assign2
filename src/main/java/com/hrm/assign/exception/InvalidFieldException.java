package com.hrm.assign.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidFieldException extends Exception{

	
	static final Logger log = LoggerFactory.getLogger(InvalidFieldException.class);
	
	private static final long serialVersionUID = 4L;

	public InvalidFieldException(String msg) {
		super(msg);
		
		log.error("Invalid Field Exception {}", msg);
	}
}
