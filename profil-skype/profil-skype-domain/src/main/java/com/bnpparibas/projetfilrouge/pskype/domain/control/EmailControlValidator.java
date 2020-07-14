package com.bnpparibas.projetfilrouge.pskype.domain.control;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailControlValidator implements ConstraintValidator<EmailControl, String> {

	public void initialize(EmailControl constraintAnnotation) {
		
	}
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		boolean result = true;
		if (email == null) {
			return result;
		}
	   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
	}
}
