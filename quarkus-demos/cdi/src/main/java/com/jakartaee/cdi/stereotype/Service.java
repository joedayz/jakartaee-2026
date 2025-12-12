package com.jakartaee.cdi.stereotype;

import com.jakartaee.cdi.interceptor.Loggable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Stereotype;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Stereotype que agrupa múltiples anotaciones CDI.
 * Equivale a @ApplicationScoped + @Loggable.
 * Demuestra cómo usar stereotypes para reducir repetición de anotaciones.
 */
@ApplicationScoped
@Loggable
@Stereotype
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface Service {
}

