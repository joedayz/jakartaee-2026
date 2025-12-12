package com.jakartaee.cdi.qualifier;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Qualifier para identificar servicios relacionados con Heroes.
 * Permite tener múltiples implementaciones del mismo tipo y seleccionar la correcta.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface HeroQualifier {
}

