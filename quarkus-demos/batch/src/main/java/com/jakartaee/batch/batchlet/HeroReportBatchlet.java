package com.jakartaee.batch.batchlet;

import jakarta.batch.api.Batchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.jakartaee.common.entities.Hero;

import java.util.List;
import java.util.logging.Logger;

/**
 * Batchlet que genera un reporte de todos los héroes activos.
 * Un Batchlet es útil para tareas simples que no requieren procesamiento por lotes.
 */
@Named("HeroReportBatchlet")
@Dependent
public class HeroReportBatchlet implements Batchlet {
    
    private static final Logger logger = Logger.getLogger(HeroReportBatchlet.class.getName());
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public String process() throws Exception {
        logger.info("=== Iniciando generación de reporte de héroes ===");
        
        try {
            // Consultar todos los héroes activos
            List<Hero> activeHeroes = entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.isActive = true ORDER BY h.powerLevel DESC", 
                Hero.class
            ).getResultList();
            
            logger.info(String.format("Total de héroes activos encontrados: %d", activeHeroes.size()));
            
            // Generar estadísticas del reporte
            int totalHeroes = activeHeroes.size();
            double averagePowerLevel = activeHeroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .average()
                .orElse(0.0);
            
            int maxPowerLevel = activeHeroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .max()
                .orElse(0);
            
            int minPowerLevel = activeHeroes.stream()
                .mapToInt(Hero::getPowerLevel)
                .min()
                .orElse(0);
            
            // Log del reporte
            logger.info("=== REPORTE DE HÉROES ACTIVOS ===");
            logger.info(String.format("Total de héroes: %d", totalHeroes));
            logger.info(String.format("Nivel de poder promedio: %.2f", averagePowerLevel));
            logger.info(String.format("Nivel de poder máximo: %d", maxPowerLevel));
            logger.info(String.format("Nivel de poder mínimo: %d", minPowerLevel));
            logger.info("=== Lista de héroes ===");
            
            activeHeroes.forEach(hero -> 
                logger.info(String.format("- %s (Poder: %s, Nivel: %d)", 
                    hero.getName(), hero.getPower(), hero.getPowerLevel()))
            );
            
            logger.info("=== Reporte generado exitosamente ===");
            
            return BatchStatus.COMPLETED.toString();
            
        } catch (Exception e) {
            logger.severe("Error al generar reporte: " + e.getMessage());
            e.printStackTrace();
            return BatchStatus.FAILED.toString();
        }
    }
    
    @Override
    public void stop() throws Exception {
        logger.info("Deteniendo generación de reporte...");
    }
}

