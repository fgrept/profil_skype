package com.example.projetfilrouge.pskype.domain.exception;

public class SpringMailException extends Exception {
    private static final long serialVersionUID = 1L;
    private ExceptionListEnum code;

    public SpringMailException(final ExceptionListEnum code, final String message) {
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
