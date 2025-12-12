package com.jakartaee.cdi.service;

import com.jakartaee.cdi.qualifier.HeroQualifier;
import com.jakartaee.cdi.qualifier.Powerful;
import com.jakartaee.cdi.qualifier.VillainQualifier;
import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que demuestra inyección con qualifiers.
 * Usa @RequestScoped para tener una instancia por request.
 * Demuestra cómo inyectar múltiples servicios del mismo tipo usando qualifiers.
 */
@RequestScoped
public class PowerAnalysisService {
    
    @Inject
    @HeroQualifier
    HeroService heroService;
    
    @Inject
    @VillainQualifier
    VillainService villainService;
    
    /**
     * Analiza el poder promedio de héroes y villanos.
     * Demuestra el uso de servicios inyectados con qualifiers.
     */
    public Map<String, Object> analyzePowerLevels() {
        List<Hero> heroes = heroService.getAllHeroes();
        List<Villain> villains = villainService.getAllVillains();
        
        double heroAvg = heroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .average()
                .orElse(0.0);
        
        double villainAvg = villains.stream()
                .mapToInt(Villain::getPowerLevel)
                .average()
                .orElse(0.0);
        
        Map<String, Object> analysis = new HashMap<>();
        analysis.put("heroCount", heroes.size());
        analysis.put("villainCount", villains.size());
        analysis.put("heroAveragePower", heroAvg);
        analysis.put("villainAveragePower", villainAvg);
        analysis.put("powerDifference", Math.abs(heroAvg - villainAvg));
        
        return analysis;
    }
    
    /**
     * Compara héroes y villanos poderosos.
     */
    public Map<String, Object> comparePowerful() {
        List<Hero> powerfulHeroes = heroService.getPowerfulHeroes();
        List<Villain> dangerousVillains = villainService.getDangerousVillains();
        
        Map<String, Object> comparison = new HashMap<>();
        comparison.put("powerfulHeroes", powerfulHeroes.size());
        comparison.put("dangerousVillains", dangerousVillains.size());
        comparison.put("balance", powerfulHeroes.size() >= dangerousVillains.size() ? "Heroes lead" : "Villains lead");
        
        return comparison;
    }
}

