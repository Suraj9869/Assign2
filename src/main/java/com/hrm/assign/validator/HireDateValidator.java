package com.hrm.assign.validator;

import java.util.Calendar;
import java.util.Date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HireDateValidator implements ConstraintValidator<HireDate, Date>{

	@Override
	public boolean isValid(Date hireDate, ConstraintValidatorContext context) {
		
		Calendar dateInCalendar = Calendar.getInstance();
	    dateInCalendar.setTime(hireDate);
		
	    return Calendar.getInstance().get(Calendar.YEAR) - dateInCalendar.get(Calendar.YEAR) >= 0;
	}

	
}
