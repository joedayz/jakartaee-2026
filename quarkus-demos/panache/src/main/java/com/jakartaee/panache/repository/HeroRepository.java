package com.jakartaee.panache.repository;

import com.jakartaee.panache.entity.HeroEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio usando Panache Repository Pattern.
 * Implementa PanacheRepository para tener métodos mágicos disponibles.
 */
@ApplicationScoped
public class HeroRepository implements PanacheRepository<HeroEntity> {
    
    /**
     * Método personalizado: encontrar héroes poderosos (powerLevel >= 80)
     * Ejemplo de método personalizado en Repository Pattern
     */
    public List<HeroEntity> findPowerfulHeroes() {
        return find("powerLevel >= ?1", 80).list();
    }
    
    /**
     * Método personalizado: encontrar héroes por nivel mínimo
     */
    public List<HeroEntity> findByMinPowerLevel(int minLevel) {
        return find("powerLevel >= ?1 ORDER BY powerLevel DESC", minLevel).list();
    }
    
    /**
     * Método personalizado: encontrar héroes activos
     */
    public List<HeroEntity> findActiveHeroes() {
        return find("isActive", true).list();
    }
    
    /**
     * Método personalizado: encontrar héroe por nombre (case-insensitive)
     */
    public Optional<HeroEntity> findByNameIgnoreCase(String name) {
        return find("UPPER(name) = UPPER(?1)", name).firstResultOptional();
    }
    
    /**
     * Método personalizado: contar héroes poderosos
     */
    public long countPowerfulHeroes() {
        return count("powerLevel >= ?1", 80);
    }
    
    /**
     * Método personalizado: encontrar héroes con paginación
     */
    public List<HeroEntity> findPowerfulHeroesPaginated(int page, int size) {
        return find("powerLevel >= ?1 ORDER BY powerLevel DESC", 80)
                .page(page, size)
                .list();
    }
    
    /**
     * Método personalizado: actualizar nivel de poder de un héroe
     */
    public void updatePowerLevel(Long id, Integer newPowerLevel) {
        update("powerLevel = ?1 WHERE id = ?2", newPowerLevel, id);
    }
}

