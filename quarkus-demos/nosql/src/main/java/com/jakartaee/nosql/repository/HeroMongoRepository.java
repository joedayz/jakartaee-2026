package com.jakartaee.nosql.repository;

import com.jakartaee.nosql.entity.HeroMongo;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositorio MongoDB para Heroes usando Panache MongoDB.
 * Demuestra queries específicas de MongoDB.
 */
@ApplicationScoped
public class HeroMongoRepository implements PanacheMongoRepository<HeroMongo> {
    
    /**
     * Buscar héroes por nivel de poder mínimo.
     */
    public List<HeroMongo> findPowerful(int minPowerLevel) {
        return find("powerLevel >= ?1", minPowerLevel).list();
    }
    
    /**
     * Buscar héroes activos.
     */
    public List<HeroMongo> findActive() {
        return find("isActive", true).list();
    }
    
    /**
     * Buscar héroes por nombre (búsqueda parcial).
     */
    public List<HeroMongo> findByNameLike(String namePattern) {
        return find("name like ?1", namePattern).list();
    }
    
    /**
     * Buscar héroes que tengan una habilidad específica.
     * Demuestra queries en arrays de MongoDB.
     */
    public List<HeroMongo> findByAbility(String ability) {
        return find("abilities", ability).list();
    }
    
    /**
     * Buscar héroes por ciudad de ubicación.
     * Demuestra queries en documentos anidados.
     */
    public List<HeroMongo> findByCity(String city) {
        return find("location.city", city).list();
    }
    
    /**
     * Buscar héroes con misiones pendientes.
     * Demuestra queries en arrays de documentos anidados.
     */
    public List<HeroMongo> findWithPendingMissions() {
        return find("missions.status", "PENDING").list();
    }
    
    /**
     * Contar héroes por nivel de poder.
     */
    public long countByPowerLevel(int minPowerLevel) {
        return count("powerLevel >= ?1", minPowerLevel);
    }
    
    /**
     * Buscar héroes ordenados por poder descendente.
     * En Panache MongoDB, el ordenamiento se incluye en la query.
     */
    public List<HeroMongo> findAllOrderedByPower() {
        return find("order by powerLevel desc").list();
    }
}

