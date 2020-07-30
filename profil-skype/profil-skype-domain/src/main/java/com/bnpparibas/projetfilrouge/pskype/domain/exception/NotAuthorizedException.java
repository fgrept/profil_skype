package com.bnpparibas.projetfilrouge.pskype.domain.exception;

public class NotAuthorizedException extends BusinessException {

	public NotAuthorizedException(ExceptionListEnum code, String message) {
		super(code, message);
	}

}