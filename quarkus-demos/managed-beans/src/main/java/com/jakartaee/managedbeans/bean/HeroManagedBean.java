package com.jakartaee.managedbeans.bean;

import com.jakartaee.common.entities.Hero;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Managed Bean para gestionar héroes.
 * 
 * Jakarta Managed Beans proporciona un modelo de programación simple para beans gestionados
 * por el contenedor. Aunque CDI es más moderno y poderoso, Managed Beans sigue siendo
 * parte de la especificación Jakarta EE Core Profile.
 * 
 * Características de @ManagedBean:
 * - Gestionado por el contenedor
 * - Soporta @PostConstruct y @PreDestroy
 * - Puede usar @Resource para inyección de recursos
 * - Puede usar @Inject para inyección de dependencias (si CDI está disponible)
 */
@ManagedBean
@ApplicationScoped
public class HeroManagedBean {
    
    private static final Logger logger = Logger.getLogger(HeroManagedBean.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    private String beanName;
    private int operationCount = 0;
    
    /**
     * Constructor sin parámetros requerido para Managed Beans.
     */
    public HeroManagedBean() {
        logger.info("🏗️  Constructor de HeroManagedBean llamado");
    }
    
    /**
     * Método de inicialización del ciclo de vida.
     * Se ejecuta después de la construcción y la inyección de dependencias.
     */
    @PostConstruct
    public void initialize() {
        beanName = "HeroManagedBean-" + System.currentTimeMillis();
        logger.info(String.format("✅ @PostConstruct ejecutado para %s", beanName));
        logger.info("   Managed Bean inicializado y listo para usar");
    }
    
    /**
     * Método de limpieza del ciclo de vida.
     * Se ejecuta antes de que el bean sea destruido.
     */
    @PreDestroy
    public void cleanup() {
        logger.info(String.format("🛑 @PreDestroy ejecutado para %s", beanName));
        logger.info(String.format("   Total de operaciones realizadas: %d", operationCount));
        logger.info("   Limpiando recursos del Managed Bean");
    }
    
    /**
     * Obtener todos los héroes.
     */
    @Transactional
    public List<Hero> getAllHeroes() {
        operationCount++;
        logger.fine(String.format("[%s] getAllHeroes() - Operación #%d", beanName, operationCount));
        return entityManager.createQuery("SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
    }
    
    /**
     * Obtener un héroe por ID.
     */
    @Transactional
    public Optional<Hero> getHeroById(Long id) {
        operationCount++;
        logger.fine(String.format("[%s] getHeroById(%d) - Operación #%d", beanName, id, operationCount));
        return Optional.ofNullable(entityManager.find(Hero.class, id));
    }
    
    /**
     * Crear un nuevo héroe.
     */
    @Transactional
    public Hero createHero(String name, String power, Integer powerLevel) {
        operationCount++;
        logger.fine(String.format("[%s] createHero(%s) - Operación #%d", beanName, name, operationCount));
        
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        entityManager.flush();
        
        return hero;
    }
    
    /**
     * Actualizar un héroe existente.
     */
    @Transactional
    public Hero updateHero(Long id, String name, String power, Integer powerLevel) {
        operationCount++;
        logger.fine(String.format("[%s] updateHero(%d) - Operación #%d", beanName, id, operationCount));
        
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            hero.setName(name);
            hero.setPower(power);
            hero.setPowerLevel(powerLevel);
            entityManager.merge(hero);
        }
        
        return hero;
    }
    
    /**
     * Eliminar un héroe.
     */
    @Transactional
    public boolean deleteHero(Long id) {
        operationCount++;
        logger.fine(String.format("[%s] deleteHero(%d) - Operación #%d", beanName, id, operationCount));
        
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            entityManager.remove(hero);
            return true;
        }
        
        return false;
    }
    
    /**
     * Obtener estadísticas del bean.
     */
    public BeanStats getStats() {
        return new BeanStats(beanName, operationCount);
    }
    
    /**
     * Clase interna para estadísticas del bean.
     */
    public static class BeanStats {
        private final String beanName;
        private final int operationCount;
        
        public BeanStats(String beanName, int operationCount) {
            this.beanName = beanName;
            this.operationCount = operationCount;
        }
        
        public String getBeanName() {
            return beanName;
        }
        
        public int getOperationCount() {
            return operationCount;
        }
    }
}

