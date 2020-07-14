package com.bnpparibas.projetfilrouge.pskype.domain.control;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
/**
 * Classe de contrôle de numéro de téléphone
 * Le numéro de téléphone accepté doit de type national (sur 10 chiffres numériques sans indicatif de région et sans séparateur)
 * Les normes E.164, RFC3966 ou international ne sont donc pas acceptés.
 * Il n'y a pas de distrinction entre le contrôle d'un numéro de téléphone mobile ou celui d'un fixe
 * @author Judicaël
 *
 */
public class PhoneControlValidator implements ConstraintValidator<PhoneControl, String> {

	public void initialize(PhoneControl constraintAnnotation) {
		//Variable à initialiser
	}
	
	@Override
	public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

		if (phoneNumber == null) {
			return true;
		}
	       try{
	            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
	            return phoneNumberUtil.isValidNumberForRegion(phoneNumberUtil.parse(phoneNumber, ""), "FR");
	        }
	        catch (NumberParseException e){
	            return false;
	        }
	}

}
