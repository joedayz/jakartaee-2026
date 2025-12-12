package com.jakartaee.annotations.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación personalizada para marcar métodos o clases relacionadas con poder de héroes.
 * Demuestra cómo crear anotaciones personalizadas en Jakarta EE.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HeroPower {
    /**
     * Nivel mínimo de poder requerido (1-100)
     */
    int minLevel() default 1;
    
    /**
     * Descripción del poder o habilidad
     */
    String description() default "";
    
    /**
     * Categoría del poder (LEGENDARY, POWERFUL, STRONG, AVERAGE, WEAK)
     */
    String category() default "AVERAGE";
}

