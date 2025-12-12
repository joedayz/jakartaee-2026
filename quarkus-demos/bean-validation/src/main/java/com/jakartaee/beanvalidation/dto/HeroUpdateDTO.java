package com.jakartaee.beanvalidation.dto;

import com.jakartaee.beanvalidation.validation.validators.ValidPowerLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar un héroe existente.
 * Demuestra validaciones para operaciones de actualización.
 */
public record HeroUpdateDTO(
    @NotNull(message = "El ID es requerido para actualizar")
    @Min(value = 1, message = "El ID debe ser mayor que 0")
    Long id,
    
    @NotBlank(message = "El nombre del héroe es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    String name,
    
    @NotBlank(message = "El poder es requerido")
    @Size(min = 3, max = 200, message = "El poder debe tener entre 3 y 200 caracteres")
    String power,
    
    @NotNull(message = "El nivel de poder es requerido")
    @ValidPowerLevel(message = "El nivel de poder debe estar entre 1 y 100")
    Integer powerLevel,
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    String description
) {
    /**
     * Constructor compacto para validaciones adicionales.
     */
    public HeroUpdateDTO {
        // Validaciones adicionales pueden ir aquí
    }
}

