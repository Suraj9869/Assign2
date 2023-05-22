package com.hrm.assign.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeException extends RuntimeException{

	private static final long serialVersionUID = 3L;

	static final Logger log = LoggerFactory.getLogger(EmployeeException.class);
	public EmployeeException(String msg) {
		
		super(msg);
		log.error("EmployeeException raised {}" , msg);
		
	}
}
