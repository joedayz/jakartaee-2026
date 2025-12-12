package com.jakartaee.beanvalidation.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para ValidHeroName.
 * Valida que el nombre del héroe:
 * - No contenga solo números
 * - No contenga caracteres especiales prohibidos
 * - Tenga al menos una letra
 */
public class HeroNameValidator implements ConstraintValidator<ValidHeroName, String> {
    
    private static final String PROHIBITED_CHARS = "[@#$%^&*()+=<>?/\\\\|{}\\[\\]~`]";
    
    @Override
    public void initialize(ValidHeroName constraintAnnotation) {
        // Inicialización si es necesaria
    }
    
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            // @NotBlank ya maneja valores nulos/vacíos
            return true;
        }
        
        // Verificar que no contenga solo números
        if (name.matches("^\\d+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "El nombre del héroe no puede contener solo números"
            ).addConstraintViolation();
            return false;
        }
        
        // Verificar que tenga al menos una letra
        if (!name.matches(".*[a-zA-Z].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "El nombre del héroe debe contener al menos una letra"
            ).addConstraintViolation();
            return false;
        }
        
        // Verificar que no contenga caracteres prohibidos
        if (name.matches(".*" + PROHIBITED_CHARS + ".*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "El nombre del héroe no puede contener caracteres especiales prohibidos (@#$%^&*()+=<>?/\\|{}[]~`)"
            ).addConstraintViolation();
            return false;
        }
        
        return true;
    }
}

