package com.jakartaee.interceptors.service;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.interceptors.interceptor.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar héroes que demuestra el uso de múltiples interceptores.
 * Este servicio usa interceptores para logging, timing, validación y caché.
 */
@ApplicationScoped
@Monitored // Interceptor de lifecycle (@PostConstruct, @PreDestroy)
@Loggable("INFO") // Interceptor de logging
public class HeroService {
    
    @Inject
    EntityManager entityManager;
    
    /**
     * Método con múltiples interceptores: Logging + Timing + Validación + Caché
     */
    @Timed(unit = "ms")
    @Validated
    @Cached(ttl = 30)
    public List<Hero> getAllHeroes() {
        return entityManager.createQuery("SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
    }
    
    /**
     * Método con Logging + Timing + Validación (sin caché porque puede cambiar)
     */
    @Timed(unit = "ms")
    @Validated
    public Optional<Hero> getHeroById(Long id) {
        return Optional.ofNullable(entityManager.find(Hero.class, id));
    }
    
    /**
     * Método con Logging + Timing + Validación
     */
    @Timed(unit = "ms")
    @Validated
    @Transactional
    public Hero createHero(String name, String power, Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        entityManager.flush();
        return hero;
    }
    
    /**
     * Método con Logging + Timing (sin validación porque puede retornar null)
     */
    @Timed(unit = "ms")
    public List<Hero> findHeroesByPowerLevel(int minPowerLevel) {
        return entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.powerLevel >= :minLevel ORDER BY h.powerLevel DESC", 
                Hero.class)
                .setParameter("minLevel", minPowerLevel)
                .getResultList();
    }
    
    /**
     * Método que demuestra que la validación falla con parámetros null
     */
    @Validated
    @Timed(unit = "ms")
    public Hero updateHero(Long id, String name, String power, Integer powerLevel) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            hero.setName(name);
            hero.setPower(power);
            hero.setPowerLevel(powerLevel);
            entityManager.merge(hero);
        }
        return hero;
    }
}

