package com.jakartaee.beanvalidation.validation.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validación personalizada para el nivel de poder de un héroe.
 * El nivel debe estar entre 1 y 100.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PowerLevelValidator.class)
@Documented
public @interface ValidPowerLevel {
    String message() default "El nivel de poder debe estar entre 1 y 100";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

