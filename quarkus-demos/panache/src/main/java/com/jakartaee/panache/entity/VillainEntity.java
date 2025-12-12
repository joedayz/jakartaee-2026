package com.jakartaee.panache.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad Villain usando Active Record Pattern.
 * Extiende PanacheEntity para tener métodos directamente disponibles.
 */
@Entity
@Table(name = "villains")
public class VillainEntity extends PanacheEntity {
    
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, unique = true)
    public String name;
    
    @NotBlank
    @Column(nullable = false)
    public String power;
    
    @NotNull
    @Column(nullable = false)
    public Integer powerLevel; // 1-100
    
    @Column(length = 500)
    public String description;
    
    @Column(name = "threat_level")
    public String threatLevel; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(name = "is_active")
    public Boolean isActive = true;
    
    @Column(name = "created_at")
    public LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public VillainEntity() {
    }
    
    public VillainEntity(String name, String power, Integer powerLevel) {
        this.name = name;
        this.power = power;
        this.powerLevel = powerLevel;
    }
    
    // Métodos estáticos usando Active Record Pattern
    // Estos métodos están disponibles directamente en la clase sin necesidad de repositorio
    
    /**
     * Encontrar villanos poderosos (powerLevel >= 80)
     * Ejemplo de método estático con Active Record Pattern
     */
    public static java.util.List<VillainEntity> findPowerful() {
        return find("powerLevel >= ?1", 80).list();
    }
    
    /**
     * Encontrar villanos por nivel de amenaza
     */
    public static java.util.List<VillainEntity> findByThreatLevel(String threatLevel) {
        return find("threatLevel", threatLevel).list();
    }
    
    /**
     * Encontrar villanos activos
     */
    public static java.util.List<VillainEntity> findActive() {
        return find("isActive", true).list();
    }
    
    /**
     * Contar villanos poderosos
     */
    public static long countPowerful() {
        return count("powerLevel >= ?1", 80);
    }
    
    /**
     * Método de instancia: activar villano
     */
    public void activate() {
        this.isActive = true;
        this.persist();
    }
    
    /**
     * Método de instancia: desactivar villano
     */
    public void deactivate() {
        this.isActive = false;
        this.persist();
    }
}

