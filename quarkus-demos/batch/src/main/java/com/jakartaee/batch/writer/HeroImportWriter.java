package com.jakartaee.batch.writer;

import jakarta.batch.api.chunk.ItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.jakartaee.common.entities.Hero;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * ItemWriter que escribe héroes importados a la base de datos.
 */
@Named("HeroImportWriter")
@Dependent
public class HeroImportWriter implements ItemWriter {
    
    private static final Logger logger = Logger.getLogger(HeroImportWriter.class.getName());
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("Abriendo HeroImportWriter...");
    }
    
    @Override
    public void close() throws Exception {
        logger.info("Cerrando HeroImportWriter...");
    }
    
    @Override
    public void writeItems(List<Object> items) throws Exception {
        logger.info(String.format("Escribiendo lote de %d héroes a la base de datos...", items.size()));
        
        int saved = 0;
        int skipped = 0;
        
        for (Object item : items) {
            if (item instanceof Hero hero) {
                try {
                    // Verificar si el héroe ya existe
                    Hero existing = entityManager.createQuery(
                        "SELECT h FROM Hero h WHERE h.name = :name", Hero.class
                    ).setParameter("name", hero.getName())
                     .getResultStream()
                     .findFirst()
                     .orElse(null);
                    
                    if (existing != null) {
                        logger.info(String.format("Héroe '%s' ya existe, actualizando...", hero.getName()));
                        existing.setPower(hero.getPower());
                        existing.setPowerLevel(hero.getPowerLevel());
                        existing.setIsActive(hero.getIsActive());
                        entityManager.merge(existing);
                    } else {
                        logger.info(String.format("Guardando nuevo héroe: %s", hero.getName()));
                        entityManager.persist(hero);
                    }
                    
                    saved++;
                    
                } catch (Exception e) {
                    logger.severe(String.format("Error al guardar héroe %s: %s", hero.getName(), e.getMessage()));
                    skipped++;
                }
            }
        }
        
        // Hacer commit del lote
        entityManager.flush();
        
        logger.info(String.format("Lote procesado: %d guardados, %d omitidos", saved, skipped));
    }
    
    @Override
    public Serializable checkpointInfo() throws Exception {
        return null; // No necesitamos checkpoint para escritura
    }
}

