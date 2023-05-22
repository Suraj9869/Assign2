package com.hrm.assign.validator;

import java.util.Calendar;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthDateValidator implements ConstraintValidator<BirthDate, Date>{

	@Override
	public boolean isValid(Date valueToValidate, ConstraintValidatorContext context) {
		
		Calendar dateInCalendar = Calendar.getInstance();
	    dateInCalendar.setTime(valueToValidate);
		
	    return Calendar.getInstance().get(Calendar.YEAR) - dateInCalendar.get(Calendar.YEAR) >= 20;
	}

	
	
}
