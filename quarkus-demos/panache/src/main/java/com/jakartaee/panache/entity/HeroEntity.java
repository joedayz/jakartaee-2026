package com.jakartaee.panache.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Entidad Hero usando Repository Pattern.
 * Esta entidad NO extiende PanacheEntity, se usa con PanacheRepository.
 */
@Entity
@Table(name = "heroes")
public class HeroEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
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
    public HeroEntity() {
    }
    
    public HeroEntity(String name, String power, Integer powerLevel) {
        this.name = name;
        this.power = power;
        this.powerLevel = powerLevel;
    }
}

