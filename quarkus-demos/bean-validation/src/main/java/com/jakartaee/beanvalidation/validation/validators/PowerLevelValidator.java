package com.jakartaee.beanvalidation.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para ValidPowerLevel.
 * Valida que el nivel de poder esté entre 1 y 100.
 */
public class PowerLevelValidator implements ConstraintValidator<ValidPowerLevel, Integer> {
    
    private static final int MIN_POWER_LEVEL = 1;
    private static final int MAX_POWER_LEVEL = 100;
    
    @Override
    public void initialize(ValidPowerLevel constraintAnnotation) {
        // Inicialización si es necesaria
    }
    
    @Override
    public boolean isValid(Integer powerLevel, ConstraintValidatorContext context) {
        if (powerLevel == null) {
            // @NotNull ya maneja valores nulos, así que retornamos true
            return true;
        }
        
        boolean isValid = powerLevel >= MIN_POWER_LEVEL && powerLevel <= MAX_POWER_LEVEL;
        
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                String.format("El nivel de poder debe estar entre %d y %d, pero se recibió: %d", 
                    MIN_POWER_LEVEL, MAX_POWER_LEVEL, powerLevel)
            ).addConstraintViolation();
        }
        
        return isValid;
    }
}

