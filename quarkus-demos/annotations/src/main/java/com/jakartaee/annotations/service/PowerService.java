package com.jakartaee.annotations.service;

import com.jakartaee.annotations.annotation.HeroPower;
import com.jakartaee.annotations.annotation.Loggable;
import com.jakartaee.common.entities.Hero;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servicio que demuestra el uso de anotaciones personalizadas para categorizar
 * y gestionar niveles de poder de héroes.
 */
@ApplicationScoped
@HeroPower(category = "POWER_MANAGEMENT", description = "Servicio de gestión de poder")
public class PowerService {
    
    private static final Logger logger = Logger.getLogger(PowerService.class.getName());
    
    private final Map<String, String> powerCategories = new HashMap<>();
    
    @PostConstruct
    public void init() {
        logger.info("=== Inicializando PowerService (@PostConstruct) ===");
        
        powerCategories.put("LEGENDARY", "90-100");
        powerCategories.put("POWERFUL", "80-89");
        powerCategories.put("STRONG", "60-79");
        powerCategories.put("AVERAGE", "40-59");
        powerCategories.put("WEAK", "1-39");
        
        logger.info("PowerService inicializado con categorías de poder");
    }
    
    /**
     * Método con múltiples anotaciones personalizadas.
     */
    @HeroPower(minLevel = 90, category = "LEGENDARY", description = "Categorizar héroes legendarios")
    @Loggable(level = "INFO", includeParameters = true, includeResult = true)
    public String categorizePower(Hero hero) {
        if (hero == null || hero.getPowerLevel() == null) {
            return "UNKNOWN";
        }
        
        int level = hero.getPowerLevel();
        
        if (level >= 90) return "LEGENDARY";
        if (level >= 80) return "POWERFUL";
        if (level >= 60) return "STRONG";
        if (level >= 40) return "AVERAGE";
        return "WEAK";
    }
    
    /**
     * Método con anotación personalizada que valida nivel mínimo.
     */
    @HeroPower(minLevel = 50, description = "Verificar si un héroe es competente")
    @Loggable(level = "DEBUG")
    public boolean isCompetentHero(Hero hero) {
        return hero != null 
                && hero.getPowerLevel() != null 
                && hero.getPowerLevel() >= 50;
    }
    
    public Map<String, String> getPowerCategories() {
        return new HashMap<>(powerCategories);
    }
}

