package com.example.projetfilrouge.pskype.domain.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneControlValidator.class)
public @interface PhoneControl {
    String message() default "Numéro de téléphone non valide selon la norme RFC";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
