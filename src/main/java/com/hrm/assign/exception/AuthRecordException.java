package com.hrm.assign.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthRecordException extends RuntimeException {


	private static final long serialVersionUID = 2L;
	static final Logger log = LoggerFactory.getLogger(AuthRecordException.class);
	
	public AuthRecordException(String msg) {
		
		super(msg);
		log.error("AuthRecordException raised {}" , msg);
		
	}
}
