package com.jakartaee.annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para habilitar logging automático de métodos.
 * Demuestra cómo crear anotaciones para cross-cutting concerns.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    /**
     * Nivel de log (TRACE, DEBUG, INFO, WARN, ERROR)
     */
    String level() default "INFO";
    
    /**
     * Incluir parámetros en el log
     */
    boolean includeParameters() default true;
    
    /**
     * Incluir valor de retorno en el log
     */
    boolean includeResult() default true;
}

