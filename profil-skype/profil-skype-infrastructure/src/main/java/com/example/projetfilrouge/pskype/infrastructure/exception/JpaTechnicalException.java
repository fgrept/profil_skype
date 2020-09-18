package com.example.projetfilrouge.pskype.infrastructure.exception;

public class JpaTechnicalException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private JpaExceptionListEnum jpaExceptionListEnum;

    public JpaTechnicalException(final JpaExceptionListEnum jpaExceptionListEnum, final String message){
        super(message);
        this.jpaExceptionListEnum = jpaExceptionListEnum;
    }

}
