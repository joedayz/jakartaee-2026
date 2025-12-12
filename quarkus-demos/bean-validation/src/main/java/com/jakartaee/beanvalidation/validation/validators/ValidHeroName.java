package com.jakartaee.beanvalidation.validation.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validación personalizada para el nombre de un héroe.
 * Valida que el nombre no contenga caracteres prohibidos y tenga un formato válido.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HeroNameValidator.class)
@Documented
public @interface ValidHeroName {
    String message() default "El nombre del héroe no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

