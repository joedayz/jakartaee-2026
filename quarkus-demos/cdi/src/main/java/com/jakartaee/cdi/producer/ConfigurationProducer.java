package com.jakartaee.cdi.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Default;
import java.util.logging.Logger;

/**
 * Producer methods para crear objetos que no son beans CDI por sí mismos.
 * Demuestra cómo usar @Produces para crear instancias configurables.
 */
@ApplicationScoped
public class ConfigurationProducer {
    
    private static final Logger logger = Logger.getLogger(ConfigurationProducer.class.getName());
    
    /**
     * Producer method para crear un Logger específico de la aplicación.
     * Demuestra cómo crear objetos que pueden ser inyectados.
     */
    @Produces
    @ApplicationScoped
    @Default
    public Logger produceApplicationLogger() {
        Logger appLogger = Logger.getLogger("com.jakartaee.cdi");
        logger.info("Produciendo Logger para la aplicación");
        return appLogger;
    }
    
    /**
     * Producer method para configuración de la aplicación.
     * En un caso real, esto podría leer de application.properties.
     */
    @Produces
    @ApplicationScoped
    @Default
    public ApplicationConfig produceApplicationConfig() {
        ApplicationConfig config = new ApplicationConfig();
        config.setMaxHeroes(100);
        config.setMaxVillains(100);
        config.setPowerThreshold(80);
        logger.info("Produciendo ApplicationConfig");
        return config;
    }
    
    /**
     * Clase de configuración simple.
     */
    public static class ApplicationConfig {
        private int maxHeroes;
        private int maxVillains;
        private int powerThreshold;
        
        public int getMaxHeroes() {
            return maxHeroes;
        }
        
        public void setMaxHeroes(int maxHeroes) {
            this.maxHeroes = maxHeroes;
        }
        
        public int getMaxVillains() {
            return maxVillains;
        }
        
        public void setMaxVillains(int maxVillains) {
            this.maxVillains = maxVillains;
        }
        
        public int getPowerThreshold() {
            return powerThreshold;
        }
        
        public void setPowerThreshold(int powerThreshold) {
            this.powerThreshold = powerThreshold;
        }
    }
}

