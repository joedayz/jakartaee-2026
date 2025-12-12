package com.jakartaee.batch.processor;

import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import com.jakartaee.common.entities.Hero;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * ItemProcessor que calcula estadísticas de poder para cada héroe.
 * Transforma un Hero en un Map con estadísticas calculadas.
 */
@Named("PowerStatisticsProcessor")
@Dependent
public class PowerStatisticsProcessor implements ItemProcessor {
    
    private static final Logger logger = Logger.getLogger(PowerStatisticsProcessor.class.getName());
    
    @Override
    public Object processItem(Object item) throws Exception {
        if (!(item instanceof Hero hero)) {
            return null;
        }
        
        // Calcular estadísticas
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("heroId", hero.getId());
        statistics.put("heroName", hero.getName());
        statistics.put("powerLevel", hero.getPowerLevel());
        statistics.put("powerCategory", categorizePower(hero.getPowerLevel()));
        statistics.put("isPowerful", hero.getPowerLevel() >= 80);
        statistics.put("isAverage", hero.getPowerLevel() >= 50 && hero.getPowerLevel() < 80);
        statistics.put("isWeak", hero.getPowerLevel() < 50);
        
        logger.fine(String.format("Procesando estadísticas para %s: Nivel %d (%s)", 
            hero.getName(), hero.getPowerLevel(), statistics.get("powerCategory")));
        
        return statistics;
    }
    
    private String categorizePower(int powerLevel) {
        if (powerLevel >= 90) {
            return "LEGENDARY";
        } else if (powerLevel >= 80) {
            return "POWERFUL";
        } else if (powerLevel >= 60) {
            return "STRONG";
        } else if (powerLevel >= 40) {
            return "AVERAGE";
        } else {
            return "WEAK";
        }
    }
}

