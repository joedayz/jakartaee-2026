package com.jakartaee.batch.reader;

import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import com.jakartaee.common.entities.Hero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ItemReader que simula la lectura de héroes desde un archivo CSV.
 * En una implementación real, esto leería desde un archivo CSV real.
 */
@Named("HeroImportReader")
@Dependent
public class HeroImportReader implements ItemReader {
    
    private static final Logger logger = Logger.getLogger(HeroImportReader.class.getName());
    
    private List<Hero> heroesToImport;
    private int index = 0;
    
    @Override
    public void open(Serializable checkpoint) throws Exception {
        logger.info("Abriendo HeroImportReader (simulando lectura de CSV)...");
        
        // Simular datos de un archivo CSV
        // En una implementación real, leerías desde un archivo CSV usando BufferedReader
        heroesToImport = new ArrayList<>();
        heroesToImport.add(new Hero("Superman", "Super fuerza, vuelo, visión de rayos X", 95));
        heroesToImport.add(new Hero("Batman", "Inteligencia superior, artes marciales", 85));
        heroesToImport.add(new Hero("Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90));
        heroesToImport.add(new Hero("Flash", "Super velocidad", 88));
        heroesToImport.add(new Hero("Green Lantern", "Anillo de poder", 87));
        heroesToImport.add(new Hero("Aquaman", "Control del agua, comunicación marina", 82));
        heroesToImport.add(new Hero("Cyborg", "Tecnología avanzada, fuerza sobrehumana", 80));
        heroesToImport.add(new Hero("Martian Manhunter", "Cambio de forma, telepatía", 92));
        
        logger.info(String.format("Total de héroes a importar: %d", heroesToImport.size()));
        
        // Restaurar checkpoint si existe
        if (checkpoint != null) {
            index = (Integer) checkpoint;
            logger.info(String.format("Reanudando desde índice: %d", index));
        }
    }
    
    @Override
    public void close() throws Exception {
        logger.info("Cerrando HeroImportReader...");
        heroesToImport = null;
        index = 0;
    }
    
    @Override
    public Object readItem() throws Exception {
        if (heroesToImport == null || index >= heroesToImport.size()) {
            return null; // Fin de los datos
        }
        
        Hero hero = heroesToImport.get(index);
        index++;
        
        logger.fine(String.format("Leyendo héroe para importar: %s (índice: %d)", hero.getName(), index - 1));
        
        return hero;
    }
    
    @Override
    public Serializable checkpointInfo() throws Exception {
        return index; // Guardar el índice actual como checkpoint
    }
}

