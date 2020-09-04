package com.example.projetfilrouge.pskype.domain.exception;

public abstract class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExceptionListEnum code;

	public BusinessException(final ExceptionListEnum code, final String message) {
		super(message);
		this.code = code;
	}

	public ExceptionListEnum getCode() {
		return code;
	}

	public void setCode(ExceptionListEnum code) {
		this.code = code;
	}

}
