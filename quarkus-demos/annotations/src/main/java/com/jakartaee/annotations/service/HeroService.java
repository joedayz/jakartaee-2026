package com.jakartaee.annotations.service;

import com.jakartaee.annotations.annotation.HeroPower;
import com.jakartaee.annotations.annotation.Loggable;
import com.jakartaee.common.entities.Hero;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servicio que demuestra el uso de anotaciones estándar de Jakarta:
 * - @PostConstruct: Inicialización después de la construcción
 * - @PreDestroy: Limpieza antes de la destrucción
 * - @Resource: Inyección de recursos
 * - Anotaciones personalizadas: @HeroPower, @Loggable
 */
@ApplicationScoped
@HeroPower(category = "SERVICE", description = "Servicio de gestión de héroes")
public class HeroService {
    
    // Logger: En Quarkus se puede inyectar directamente o usar Logger.getLogger
    private Logger logger;
    
    private final List<Hero> heroes = new ArrayList<>();
    private boolean initialized = false;
    
    /**
     * @PostConstruct: Se ejecuta después de la construcción del bean.
     * Útil para inicialización que requiere que todas las dependencias estén inyectadas.
     */
    @PostConstruct
    public void init() {
        if (logger == null) {
            logger = Logger.getLogger(HeroService.class.getName());
        }
        
        logger.info("=== Inicializando HeroService (@PostConstruct) ===");
        
        // Inicializar algunos héroes de ejemplo
        heroes.add(new Hero("Superman", "Super fuerza, vuelo, visión de rayos X", 95));
        heroes.add(new Hero("Batman", "Inteligencia superior, artes marciales", 85));
        heroes.add(new Hero("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90));
        heroes.add(new Hero("Flash", "Super velocidad", 88));
        heroes.add(new Hero("Green Lantern", "Anillo de poder", 87));
        
        initialized = true;
        logger.info(String.format("HeroService inicializado con %d héroes", heroes.size()));
    }
    
    /**
     * @PreDestroy: Se ejecuta antes de destruir el bean.
     * Útil para liberar recursos, cerrar conexiones, etc.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("=== Limpiando HeroService (@PreDestroy) ===");
        heroes.clear();
        initialized = false;
        logger.info("HeroService limpiado correctamente");
    }
    
    /**
     * Método con anotaciones personalizadas.
     * @HeroPower: Marca que este método maneja héroes poderosos
     * @Loggable: Habilita logging automático
     */
    @HeroPower(minLevel = 80, category = "POWERFUL", description = "Obtener héroes poderosos")
    @Loggable(level = "INFO", includeParameters = false, includeResult = true)
    public List<Hero> getPowerfulHeroes() {
        return heroes.stream()
                .filter(h -> h.getPowerLevel() != null && h.getPowerLevel() >= 80)
                .toList();
    }
    
    /**
     * Método con anotación personalizada para logging.
     */
    @Loggable(level = "DEBUG", includeParameters = true, includeResult = true)
    public Hero getHeroByName(String name) {
        return heroes.stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Método que demuestra el uso de @Generated (marcado como código generado).
     * En una implementación real, esto sería generado por un procesador de anotaciones.
     */
    @jakarta.annotation.Generated(value = "HeroServiceGenerator", date = "2025-12-12")
    public List<Hero> getAllHeroes() {
        return new ArrayList<>(heroes);
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public int getHeroCount() {
        return heroes.size();
    }
}

