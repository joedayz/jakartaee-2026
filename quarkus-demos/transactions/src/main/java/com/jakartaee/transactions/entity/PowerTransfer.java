package com.jakartaee.transactions.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una transferencia de poder entre héroes.
 * Demuestra operaciones transaccionales complejas.
 */
@Entity
@Table(name = "power_transfers")
public class PowerTransfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "from_hero_id", nullable = false)
    private Long fromHeroId;
    
    @Column(name = "to_hero_id", nullable = false)
    private Long toHeroId;
    
    @Column(nullable = false)
    private Integer amount; // Cantidad de poder transferida
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = TransferStatus.PENDING;
    }
    
    // Constructors
    public PowerTransfer() {
    }
    
    public PowerTransfer(Long fromHeroId, Long toHeroId, Integer amount) {
        this.fromHeroId = fromHeroId;
        this.toHeroId = toHeroId;
        this.amount = amount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFromHeroId() {
        return fromHeroId;
    }
    
    public void setFromHeroId(Long fromHeroId) {
        this.fromHeroId = fromHeroId;
    }
    
    public Long getToHeroId() {
        return toHeroId;
    }
    
    public void setToHeroId(Long toHeroId) {
        this.toHeroId = toHeroId;
    }
    
    public Integer getAmount() {
        return amount;
    }
    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    
    public TransferStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransferStatus status) {
        this.status = status;
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
    
    public enum TransferStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
}

