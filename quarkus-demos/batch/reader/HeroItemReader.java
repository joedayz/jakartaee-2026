package com.jakartaee.batch.reader;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.jakartaee.common.entities.Hero;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * ItemReader que lee héroes de la base de datos para procesamiento batch.
 */
@Named("HeroItemReader")
@Dependent
public class HeroItemReader implements ItemReader {
    
    private static final Logger logger = Logger.getLogger(HeroItemReader.class.getName());
    
    @PersistenceContext
    EntityManager entityManager;
    
    private List<Hero> heroes;
    private int index = 0;
    
    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("Abriendo HeroItemReader...");
        
        // Cargar todos los héroes de la base de datos
        heroes = entityManager.createQuery(
            "SELECT h FROM Hero h ORDER BY h.id", 
            Hero.class
        ).getResultList();
        
        logger.info(String.format("Total de héroes cargados para procesamiento: %d", heroes.size()));
        
        // Restaurar checkpoint si existe
        if (checkpoint != null) {
            index = (Integer) checkpoint;
            logger.info(String.format("Reanudando desde índice: %d", index));
        }
    }
    
    @Override
    public void close() throws Exception {
        logger.info("Cerrando HeroItemReader...");
        heroes = null;
        index = 0;
    }
    
    @Override
    public Object readItem() throws Exception {
        if (heroes == null || index >= heroes.size()) {
            return null; // Fin de los datos
        }
        
        Hero hero = heroes.get(index);
        index++;
        
        logger.fine(String.format("Leyendo héroe: %s (índice: %d)", hero.getName(), index - 1));
        
        return hero;
    }
    
    @Override
    public Serializable checkpointInfo() throws Exception {
        return index; // Guardar el índice actual como checkpoint
    }
}

