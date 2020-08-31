package com.bnpparibas.projetfilrouge.pskype.domain.exception;

public class NotAuthorizedException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotAuthorizedException(ExceptionListEnum code, String message) {
		super(code, message);
	}

}