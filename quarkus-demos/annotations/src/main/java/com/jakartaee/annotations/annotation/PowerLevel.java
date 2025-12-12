package com.jakartaee.annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para validar niveles de poder de héroes.
 * Demuestra cómo crear anotaciones de validación personalizadas.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PowerLevel {
    /**
     * Nivel mínimo permitido
     */
    int min() default 1;
    
    /**
     * Nivel máximo permitido
     */
    int max() default 100;
    
    /**
     * Mensaje de error personalizado
     */
    String message() default "El nivel de poder debe estar entre {min} y {max}";
}

