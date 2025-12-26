package com.jakartaee.interceptors.service;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.interceptors.interceptor.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que demuestra interceptores en constructores (@AroundConstruct).
 * Este servicio usa @Tracked para rastrear su creación.
 */
@ApplicationScoped
@Monitored
@Tracked // Interceptor de constructor
@Loggable("DEBUG")
public class PowerAnalysisService {
    
    @Inject
    EntityManager entityManager;
    
    private int analysisCount = 0;
    
    /**
     * Constructor que será interceptado por @Tracked
     */
    public PowerAnalysisService() {
        // Este constructor será interceptado por ConstructorInterceptor
    }
    
    /**
     * Método con múltiples interceptores encadenados.
     */
    @Timed(unit = "ms")
    @Validated
    @Cached(ttl = 60)
    public Map<String, Object> analyzePowers() {
        analysisCount++;
        
        List<Hero> heroes = entityManager.createQuery(
                "SELECT h FROM Hero h", Hero.class)
                .getResultList();
        
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("totalHeroes", heroes.size());
        analysis.put("averagePowerLevel", calculateAveragePower(heroes));
        analysis.put("maxPowerLevel", heroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .max()
                .orElse(0));
        analysis.put("minPowerLevel", heroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .min()
                .orElse(0));
        analysis.put("analysisCount", analysisCount);
        
        return analysis;
    }
    
    @Timed(unit = "ms")
    public List<String> getPowerTypes() {
        return entityManager.createQuery(
                "SELECT DISTINCT h.power FROM Hero h", String.class)
                .getResultList();
    }
    
    private double calculateAveragePower(List<Hero> heroes) {
        if (heroes.isEmpty()) {
            return 0.0;
        }
        return heroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .average()
                .orElse(0.0);
    }
}

