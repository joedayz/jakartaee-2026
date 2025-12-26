package com.jakartaee.managedbeans.service;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import com.jakartaee.managedbeans.bean.HeroManagedBean;
import com.jakartaee.managedbeans.bean.VillainManagedBean;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Servicio que demuestra cómo un Managed Bean puede inyectar otros Managed Beans.
 * 
 * Este servicio coordina batallas entre héroes y villanos usando
 * los Managed Beans de Hero y Villain.
 */
@ManagedBean
@ApplicationScoped
public class BattleService {
    
    private static final Logger logger = Logger.getLogger(BattleService.class.getName());
    
    @Inject
    HeroManagedBean heroManagedBean;
    
    @Inject
    VillainManagedBean villainManagedBean;
    
    private String beanName;
    private int battleCount = 0;
    
    public BattleService() {
        logger.info("🏗️  Constructor de BattleService llamado");
    }
    
    @PostConstruct
    public void initialize() {
        beanName = "BattleService-" + System.currentTimeMillis();
        logger.info(String.format("✅ @PostConstruct ejecutado para %s", beanName));
        logger.info("   BattleService inicializado con dependencias inyectadas");
    }
    
    @PreDestroy
    public void cleanup() {
        logger.info(String.format("🛑 @PreDestroy ejecutado para %s", beanName));
        logger.info(String.format("   Total de batallas simuladas: %d", battleCount));
    }
    
    /**
     * Simular una batalla entre un héroe y un villano.
     */
    public Map<String, Object> simulateBattle(Long heroId, Long villainId) {
        battleCount++;
        logger.info(String.format("[%s] Simulando batalla #%d entre héroe %d y villano %d", 
                beanName, battleCount, heroId, villainId));
        
        Map<String, Object> result = new HashMap<>();
        
        var heroOpt = heroManagedBean.getHeroById(heroId);
        var villainOpt = villainManagedBean.getVillainById(villainId);
        
        if (heroOpt.isEmpty() || villainOpt.isEmpty()) {
            result.put("status", "ERROR");
            result.put("message", "Héroe o villano no encontrado");
            return result;
        }
        
        Hero hero = heroOpt.get();
        Villain villain = villainOpt.get();
        
        // Lógica simple de batalla basada en powerLevel
        int heroPower = hero.getPowerLevel();
        int villainPower = villain.getPowerLevel();
        
        result.put("hero", hero.getName());
        result.put("heroPower", heroPower);
        result.put("villain", villain.getName());
        result.put("villainPower", villainPower);
        
        if (heroPower > villainPower) {
            result.put("winner", hero.getName());
            result.put("status", "HERO_WINS");
            result.put("message", String.format("%s derrota a %s!", hero.getName(), villain.getName()));
        } else if (villainPower > heroPower) {
            result.put("winner", villain.getName());
            result.put("status", "VILLAIN_WINS");
            result.put("message", String.format("%s derrota a %s!", villain.getName(), hero.getName()));
        } else {
            result.put("status", "DRAW");
            result.put("message", "¡Empate! Ambos combatientes tienen el mismo poder");
        }
        
        result.put("battleNumber", battleCount);
        
        return result;
    }
    
    /**
     * Obtener estadísticas del servicio.
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("beanName", beanName);
        stats.put("battleCount", battleCount);
        stats.put("heroBeanStats", heroManagedBean.getStats());
        return stats;
    }
}

