package com.jakartaee.nosql.repository;

import com.jakartaee.nosql.entity.VillainMongo;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Repositorio MongoDB para Villains usando Panache MongoDB.
 */
@ApplicationScoped
public class VillainMongoRepository implements PanacheMongoRepository<VillainMongo> {
    
    /**
     * Buscar villanos por nivel de amenaza.
     */
    public List<VillainMongo> findByThreatLevel(String threatLevel) {
        return find("threatLevel", threatLevel).list();
    }
    
    /**
     * Buscar villanos peligrosos (HIGH o CRITICAL).
     */
    public List<VillainMongo> findDangerous() {
        return find("threatLevel in ?1", List.of("HIGH", "CRITICAL")).list();
    }
    
    /**
     * Buscar villanos por nivel de poder mínimo.
     */
    public List<VillainMongo> findPowerful(int minPowerLevel) {
        return find("powerLevel >= ?1", minPowerLevel).list();
    }
    
    /**
     * Buscar villanos que tengan una habilidad específica.
     */
    public List<VillainMongo> findByAbility(String ability) {
        return find("abilities", ability).list();
    }
    
    /**
     * Buscar villanos por nombre de base secreta.
     * Demuestra queries en documentos anidados.
     */
    public List<VillainMongo> findBySecretBaseName(String baseName) {
        return find("secretBase.name", baseName).list();
    }
}

