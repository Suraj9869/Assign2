package com.hrm.assign.exception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, List<String>>> handleSQLException(DataIntegrityViolationException ex){
		
        Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(EmployeeException.class)
	public ResponseEntity<Map<String, List<String>>> handleEmployeeException(EmployeeException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));
        
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(ManagerException.class)
	public ResponseEntity<Map<String, List<String>>> handleManagerException(ManagerException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AdminException.class)
	public ResponseEntity<Map<String, List<String>>> handleAdminException(AdminException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(InvalidFieldException.class)
	public ResponseEntity<Map<String, List<String>>> handleInvalidFieldException(InvalidFieldException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(AuthRecordException.class)
	public ResponseEntity<Map<String, List<String>>> handleAuthRecordException(AuthRecordException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(TokenValidationException.class)
	public ResponseEntity<Map<String, List<String>>> handleTokenValidationException(TokenValidationException ex){
		
		Map<String, List<String>> errorResponse = getErrorsMap(Arrays.asList(ex.getMessage()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		
	}
	
	
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
	
    
}
