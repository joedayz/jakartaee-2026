package com.jakartaee.beanvalidation.dto;

import com.jakartaee.beanvalidation.validation.validators.ValidHeroName;
import com.jakartaee.beanvalidation.validation.validators.ValidPowerLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un nuevo héroe con validaciones avanzadas.
 * Demuestra el uso de validadores personalizados.
 * 
 * Nota: Los grupos de validación se pueden usar, pero para simplificar
 * este demo usamos el grupo Default implícito.
 */
public record HeroCreateDTO(
    @NotBlank(message = "El nombre del héroe es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @ValidHeroName(message = "El nombre del héroe no es válido")
    String name,
    
    @NotBlank(message = "El poder es requerido")
    @Size(min = 3, max = 200, message = "El poder debe tener entre 3 y 200 caracteres")
    String power,
    
    @NotNull(message = "El nivel de poder es requerido")
    @ValidPowerLevel(message = "El nivel de poder debe estar entre 1 y 100")
    Integer powerLevel,
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    String description,
    
    @Email(message = "El email debe tener un formato válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
             message = "El email debe ser válido")
    String email
) {
    /**
     * Constructor compacto para validaciones adicionales.
     */
    public HeroCreateDTO {
        // Validaciones adicionales pueden ir aquí
    }
}

