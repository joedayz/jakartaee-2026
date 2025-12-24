package com.jakartaee.transactions.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una batalla entre héroes y villanos.
 * Demuestra transacciones con múltiples operaciones.
 */
@Entity
@Table(name = "battles")
public class Battle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "hero_id", nullable = false)
    private Long heroId;
    
    @Column(name = "villain_id", nullable = false)
    private Long villainId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BattleResult result;
    
    @Column(name = "hero_power_before")
    private Integer heroPowerBefore;
    
    @Column(name = "hero_power_after")
    private Integer heroPowerAfter;
    
    @Column(name = "villain_power_before")
    private Integer villainPowerBefore;
    
    @Column(name = "villain_power_after")
    private Integer villainPowerAfter;
    
    @Column(name = "power_exchanged")
    private Integer powerExchanged;
    
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;
    
    @Column(name = "ended_at")
    private LocalDateTime endedAt;
    
    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Battle() {
    }
    
    public Battle(Long heroId, Long villainId) {
        this.heroId = heroId;
        this.villainId = villainId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getHeroId() {
        return heroId;
    }
    
    public void setHeroId(Long heroId) {
        this.heroId = heroId;
    }
    
    public Long getVillainId() {
        return villainId;
    }
    
    public void setVillainId(Long villainId) {
        this.villainId = villainId;
    }
    
    public BattleResult getResult() {
        return result;
    }
    
    public void setResult(BattleResult result) {
        this.result = result;
    }
    
    public Integer getHeroPowerBefore() {
        return heroPowerBefore;
    }
    
    public void setHeroPowerBefore(Integer heroPowerBefore) {
        this.heroPowerBefore = heroPowerBefore;
    }
    
    public Integer getHeroPowerAfter() {
        return heroPowerAfter;
    }
    
    public void setHeroPowerAfter(Integer heroPowerAfter) {
        this.heroPowerAfter = heroPowerAfter;
    }
    
    public Integer getVillainPowerBefore() {
        return villainPowerBefore;
    }
    
    public void setVillainPowerBefore(Integer villainPowerBefore) {
        this.villainPowerBefore = villainPowerBefore;
    }
    
    public Integer getVillainPowerAfter() {
        return villainPowerAfter;
    }
    
    public void setVillainPowerAfter(Integer villainPowerAfter) {
        this.villainPowerAfter = villainPowerAfter;
    }
    
    public Integer getPowerExchanged() {
        return powerExchanged;
    }
    
    public void setPowerExchanged(Integer powerExchanged) {
        this.powerExchanged = powerExchanged;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getEndedAt() {
        return endedAt;
    }
    
    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
    
    public enum BattleResult {
        HERO_WIN, VILLAIN_WIN, DRAW
    }
}

