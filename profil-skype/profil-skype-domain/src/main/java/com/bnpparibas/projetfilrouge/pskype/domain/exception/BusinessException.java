package com.bnpparibas.projetfilrouge.pskype.domain.exception;

public abstract class BusinessException extends RuntimeException {

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
