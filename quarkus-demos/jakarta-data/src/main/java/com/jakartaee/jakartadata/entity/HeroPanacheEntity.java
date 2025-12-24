package com.jakartaee.jakartadata.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad Hero usando Panache Next (Hibernate with Panache).
 * Demuestra:
 * - Extender PanacheEntity para active record pattern
 * - Repositorio anidado dentro de la entidad
 * - Combinación de Jakarta Data y Panache
 * 
 * NOTA: Panache Next es experimental. Este ejemplo muestra la estructura de la API.
 * @HQL aún no está disponible en Jakarta Data 1.0.1, así que usamos métodos de Panache tradicionales.
 */
@Entity
@Table(name = "heroes_panache")
public class HeroPanacheEntity extends PanacheEntity {
    
    @Column(nullable = false, unique = true)
    public String name;
    
    @Column(nullable = false)
    public String power;
    
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
    
    /**
     * Repositorio anidado usando Panache.
     * Demuestra la nueva API de Panache Next que combina Panache con repositorios anidados.
     * 
     * NOTA: En Panache Next experimental, se usaría @HQL para queries type-safe.
     * Por ahora usamos métodos de Panache tradicionales.
     */
    public interface Repo extends PanacheRepository<HeroPanacheEntity> {
        
        /**
         * Buscar héroes activos usando query de Panache.
         * En Panache Next experimental sería: @HQL("where isActive = true order by name")
         */
        default List<HeroPanacheEntity> findActive() {
            return find("isActive = true order by name").list();
        }
        
        /**
         * Buscar héroes poderosos usando query de Panache con parámetros.
         * En Panache Next experimental sería: @HQL("where powerLevel >= :minLevel order by powerLevel desc")
         */
        default List<HeroPanacheEntity> findPowerful(int minLevel) {
            return find("powerLevel >= ?1 order by powerLevel desc", minLevel).list();
        }
        
        /**
         * Buscar héroes por rango de poder usando query de Panache.
         * En Panache Next experimental sería: @HQL("where powerLevel between :minLevel and :maxLevel order by powerLevel desc")
         */
        default List<HeroPanacheEntity> findByPowerRange(int minLevel, int maxLevel) {
            return find("powerLevel between ?1 and ?2 order by powerLevel desc", minLevel, maxLevel).list();
        }
        
        /**
         * Contar héroes activos usando query de Panache.
         * En Panache Next experimental sería: @HQL("select count(*) from HeroPanacheEntity where isActive = true")
         */
        default long countActive() {
            return count("isActive = true");
        }
        
        /**
         * Eliminar por nombre usando query de Panache.
         * En Panache Next experimental sería: @HQL("delete from HeroPanacheEntity where name = :name")
         */
        default long deleteByName(String name) {
            return delete("name = ?1", name);
        }
        
        /**
         * Obtener todos los héroes ordenados por poder usando query de Panache.
         * En Panache Next experimental sería: @HQL("order by powerLevel desc")
         */
        default List<HeroPanacheEntity> findAllOrderedByPower() {
            return find("order by powerLevel desc").list();
        }
    }
}

