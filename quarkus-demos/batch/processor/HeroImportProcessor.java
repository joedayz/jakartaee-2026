package com.jakartaee.batch.processor;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import com.jakartaee.common.entities.Hero;

import java.util.logging.Logger;

/**
 * ItemProcessor que valida y prepara héroes para importación.
 * Puede filtrar o transformar datos antes de escribirlos.
 */
@Named("HeroImportProcessor")
@Dependent
public class HeroImportProcessor implements ItemProcessor {
    
    private static final Logger logger = Logger.getLogger(HeroImportProcessor.class.getName());
    
    @Override
    public Object processItem(Object item) throws Exception {
        if (!(item instanceof Hero hero)) {
            logger.warning("Item no es una instancia de Hero, omitiendo...");
            return null;
        }
        
        // Validar datos del héroe
        if (hero.getName() == null || hero.getName().trim().isEmpty()) {
            logger.warning("Héroe sin nombre, omitiendo...");
            return null;
        }
        
        if (hero.getPowerLevel() == null || hero.getPowerLevel() < 1 || hero.getPowerLevel() > 100) {
            logger.warning(String.format("Nivel de poder inválido para %s: %d, ajustando...", 
                hero.getName(), hero.getPowerLevel()));
            // Ajustar nivel de poder si está fuera de rango
            if (hero.getPowerLevel() == null || hero.getPowerLevel() < 1) {
                hero.setPowerLevel(50); // Valor por defecto
            } else if (hero.getPowerLevel() > 100) {
                hero.setPowerLevel(100); // Máximo permitido
            }
        }
        
        // Asegurar que el héroe esté activo
        hero.setIsActive(true);
        
        logger.fine(String.format("Procesando héroe para importación: %s (Nivel: %d)", 
            hero.getName(), hero.getPowerLevel()));
        
        return hero;
    }
}

