package com.hrm.assign.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagerException extends RuntimeException{

	private static final long serialVersionUID = 5L;

	static final Logger log = LoggerFactory.getLogger(ManagerException.class);
	public ManagerException(String msg) {
		super(msg);
		
		log.error("ManagerException raised {}" ,msg);
	}
}
