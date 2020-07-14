package com.bnpparibas.projetfilrouge.pskype.domain.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailControlValidator.class)
public @interface EmailControl {
    String message() default "Email non valide selon la norme RFC";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
