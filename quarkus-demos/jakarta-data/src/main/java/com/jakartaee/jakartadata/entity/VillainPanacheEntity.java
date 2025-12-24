package com.jakartaee.jakartadata.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Villain usando Panache Next.
 * Demuestra repositorio anidado dentro de la entidad.
 * 
 * NOTA: @HQL aún no está disponible, así que usamos métodos de Panache tradicionales.
 */
@Entity
@Table(name = "villains_panache")
public class VillainPanacheEntity extends PanacheEntity {
    
    @Column(nullable = false, unique = true)
    public String name;
    
    @Column(nullable = false)
    public String power;
    
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
    
    /**
     * Repositorio anidado usando Panache.
     * En Panache Next experimental se usaría @HQL para queries type-safe.
     */
    public interface Repo extends PanacheRepository<VillainPanacheEntity> {
        
        /**
         * Buscar villanos peligrosos usando query de Panache.
         * En Panache Next experimental sería: @HQL("where threatLevel in ('HIGH', 'CRITICAL') order by threatLevel desc, powerLevel desc")
         */
        default List<VillainPanacheEntity> findDangerous() {
            return find("threatLevel in ('HIGH', 'CRITICAL') order by threatLevel desc, powerLevel desc").list();
        }
        
        /**
         * Buscar villanos por nivel de amenaza usando query de Panache.
         * En Panache Next experimental sería: @HQL("where threatLevel = :threatLevel order by powerLevel desc")
         */
        default List<VillainPanacheEntity> findByThreatLevel(String threatLevel) {
            return find("threatLevel = ?1 order by powerLevel desc", threatLevel).list();
        }
        
        /**
         * Buscar villanos poderosos usando query de Panache.
         * En Panache Next experimental sería: @HQL("where powerLevel >= :minLevel order by powerLevel desc")
         */
        default List<VillainPanacheEntity> findPowerful(int minLevel) {
            return find("powerLevel >= ?1 order by powerLevel desc", minLevel).list();
        }
        
        /**
         * Contar villanos peligrosos usando query de Panache.
         * En Panache Next experimental sería: @HQL("select count(*) from VillainPanacheEntity where threatLevel in ('HIGH', 'CRITICAL')")
         */
        default long countDangerous() {
            return count("threatLevel in ('HIGH', 'CRITICAL')");
        }
    }
}

