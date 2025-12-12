package com.jakartaee.common.dto;

/**
 * DTO para transferir datos de Hero usando Java 21 Records.
 * Usado en múltiples demos para mostrar diferentes specs.
 * 
 * Records proporcionan:
 * - Constructor compacto automático
 * - Getters automáticos (id(), name(), etc.)
 * - equals(), hashCode(), toString() automáticos
 * - Inmutabilidad por defecto
 */
public record HeroDTO(
    Long id,
    String name,
    String power,
    Integer powerLevel,
    String description,
    Boolean isActive
) {
    /**
     * Constructor compacto para valores por defecto.
     */
    public HeroDTO {
        // Validación o inicialización si es necesario
        if (isActive == null) {
            isActive = true;
        }
    }
    
    /**
     * Constructor de conveniencia para crear sin description e isActive.
     */
    public HeroDTO(Long id, String name, String power, Integer powerLevel) {
        this(id, name, power, powerLevel, null, true);
    }
}

