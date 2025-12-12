package com.jakartaee.common.dto;

/**
 * DTO para transferir datos de Villain usando Java 21 Records.
 * Usado en múltiples demos para mostrar diferentes specs.
 * 
 * Records proporcionan:
 * - Constructor compacto automático
 * - Getters automáticos (id(), name(), etc.)
 * - equals(), hashCode(), toString() automáticos
 * - Inmutabilidad por defecto
 */
public record VillainDTO(
    Long id,
    String name,
    String power,
    Integer powerLevel,
    String description,
    String threatLevel,
    Boolean isActive
) {
    /**
     * Constructor compacto para valores por defecto.
     */
    public VillainDTO {
        // Validación o inicialización si es necesario
        if (isActive == null) {
            isActive = true;
        }
        if (threatLevel == null) {
            threatLevel = "MEDIUM";
        }
    }
    
    /**
     * Constructor de conveniencia para crear sin description, threatLevel e isActive.
     */
    public VillainDTO(Long id, String name, String power, Integer powerLevel) {
        this(id, name, power, powerLevel, null, "MEDIUM", true);
    }
}

