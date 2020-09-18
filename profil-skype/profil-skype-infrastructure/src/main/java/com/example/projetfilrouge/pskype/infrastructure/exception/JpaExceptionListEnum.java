package com.example.projetfilrouge.pskype.infrastructure.exception;

public enum JpaExceptionListEnum {

    READ_ACCESS("Accès en lecture"),
    WRITE_ACCESS("Accès en écritrure");

    private String accessType;

    JpaExceptionListEnum(final String accessType) {
        this.accessType = accessType;
    }
}
