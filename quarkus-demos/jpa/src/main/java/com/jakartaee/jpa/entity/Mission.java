package com.jakartaee.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Mission que representa una misión de un héroe.
 * Demuestra relaciones ManyToOne con Hero.
 */
@Entity
@Table(name = "missions")
@NamedQueries({
    @NamedQuery(
        name = "Mission.findByHeroId",
        query = "SELECT m FROM Mission m WHERE m.hero.id = :heroId ORDER BY m.completedAt DESC"
    ),
    @NamedQuery(
        name = "Mission.findCompleted",
        query = "SELECT m FROM Mission m WHERE m.completed = true ORDER BY m.completedAt DESC"
    ),
    @NamedQuery(
        name = "Mission.findByStatus",
        query = "SELECT m FROM Mission m WHERE m.status = :status ORDER BY m.createdAt DESC"
    ),
    @NamedQuery(
        name = "Mission.countByHeroId",
        query = "SELECT COUNT(m) FROM Mission m WHERE m.hero.id = :heroId"
    )
})
public class Mission implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionStatus status = MissionStatus.PENDING;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hero_id", nullable = false)
    private HeroJPA hero;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PostLoad
    protected void onLoad() {
        // Ejemplo de callback PostLoad
        if (completed && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
    
    // Constructors
    public Mission() {
    }
    
    public Mission(String title, String description, HeroJPA hero) {
        this.title = title;
        this.description = description;
        this.hero = hero;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public MissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(MissionStatus status) {
        this.status = status;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
        if (completed && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
    
    public HeroJPA getHero() {
        return hero;
    }
    
    public void setHero(HeroJPA hero) {
        this.hero = hero;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return Objects.equals(id, mission.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", completed=" + completed +
                '}';
    }
    
    /**
     * Enum para el estado de la misión.
     */
    public enum MissionStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED, CANCELLED
    }
}

