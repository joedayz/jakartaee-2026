package com.jakartaee.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Hero para demostrar características avanzadas de JPA.
 * Incluye:
 * - Named Queries
 * - Relaciones OneToMany con Mission
 * - Lifecycle callbacks
 * - Mapeo de columnas personalizado
 */
@Entity
@Table(name = "heroes_jpa")
@NamedQueries({
    @NamedQuery(
        name = "HeroJPA.findAll",
        query = "SELECT h FROM HeroJPA h ORDER BY h.name"
    ),
    @NamedQuery(
        name = "HeroJPA.findByName",
        query = "SELECT h FROM HeroJPA h WHERE h.name = :name"
    ),
    @NamedQuery(
        name = "HeroJPA.findByPowerLevel",
        query = "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel ORDER BY h.powerLevel DESC"
    ),
    @NamedQuery(
        name = "HeroJPA.findActive",
        query = "SELECT h FROM HeroJPA h WHERE h.isActive = true ORDER BY h.name"
    ),
    @NamedQuery(
        name = "HeroJPA.findByPowerRange",
        query = "SELECT h FROM HeroJPA h WHERE h.powerLevel BETWEEN :minLevel AND :maxLevel ORDER BY h.powerLevel DESC"
    ),
    @NamedQuery(
        name = "HeroJPA.countByPowerLevel",
        query = "SELECT COUNT(h) FROM HeroJPA h WHERE h.powerLevel >= :minLevel"
    ),
    @NamedQuery(
        name = "HeroJPA.updatePowerLevel",
        query = "UPDATE HeroJPA h SET h.powerLevel = :newLevel WHERE h.id = :id"
    )
})
public class HeroJPA implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del héroe es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @NotBlank(message = "El poder es requerido")
    @Column(nullable = false, length = 200)
    private String power;
    
    @NotNull(message = "El nivel de poder es requerido")
    @Column(nullable = false, name = "power_level")
    private Integer powerLevel; // 1-100
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * Relación OneToMany con Mission.
     * Demuestra relaciones JPA y lazy loading.
     */
    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Mission> missions = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    @Column(name = "version")
    private Long version; // Optimistic locking
    
    /**
     * Lifecycle callback: PrePersist
     * Se ejecuta antes de persistir la entidad.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Lifecycle callback: PreUpdate
     * Se ejecuta antes de actualizar la entidad.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Lifecycle callback: PostLoad
     * Se ejecuta después de cargar la entidad desde la base de datos.
     */
    @PostLoad
    protected void onLoad() {
        // Ejemplo: inicializar colecciones lazy si es necesario
        if (missions == null) {
            missions = new ArrayList<>();
        }
    }
    
    // Constructors
    public HeroJPA() {
    }
    
    public HeroJPA(String name, String power, Integer powerLevel) {
        this.name = name;
        this.power = power;
        this.powerLevel = powerLevel;
    }
    
    // Helper methods para manejar la relación bidireccional
    public void addMission(Mission mission) {
        missions.add(mission);
        mission.setHero(this);
    }
    
    public void removeMission(Mission mission) {
        missions.remove(mission);
        mission.setHero(null);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPower() {
        return power;
    }
    
    public void setPower(String power) {
        this.power = power;
    }
    
    public Integer getPowerLevel() {
        return powerLevel;
    }
    
    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public List<Mission> getMissions() {
        return missions;
    }
    
    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "HeroJPA{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", power='" + power + '\'' +
                ", powerLevel=" + powerLevel +
                ", isActive=" + isActive +
                ", missionsCount=" + (missions != null ? missions.size() : 0) +
                '}';
    }
}

