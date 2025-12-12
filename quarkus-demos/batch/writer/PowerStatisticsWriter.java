package com.jakartaee.batch.writer;

import jakarta.batch.api.chunk.ItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * ItemWriter que escribe las estadísticas de poder calculadas.
 * En una implementación real, esto podría escribir a una base de datos, archivo, etc.
 */
@Named("PowerStatisticsWriter")
@Dependent
public class PowerStatisticsWriter implements ItemWriter {
    
    private static final Logger logger = Logger.getLogger(PowerStatisticsWriter.class.getName());
    
    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("Abriendo PowerStatisticsWriter...");
    }
    
    @Override
    public void close() throws Exception {
        logger.info("Cerrando PowerStatisticsWriter...");
    }
    
    @Override
    public void writeItems(List<Object> items) throws Exception {
        logger.info(String.format("Escribiendo lote de %d estadísticas...", items.size()));
        
        for (Object item : items) {
            if (item instanceof Map<?, ?> statistics) {
                String heroName = (String) statistics.get("heroName");
                Integer powerLevel = (Integer) statistics.get("powerLevel");
                String category = (String) statistics.get("powerCategory");
                
                logger.info(String.format("  - %s: Nivel %d (%s)", heroName, powerLevel, category));
                
                // En una implementación real, aquí escribirías a una tabla de estadísticas
                // o a un archivo de reporte
            }
        }
        
        logger.info("Lote de estadísticas escrito exitosamente");
    }
    
    @Override
    public Serializable checkpointInfo() throws Exception {
        return null; // No necesitamos checkpoint para escritura
    }
}

