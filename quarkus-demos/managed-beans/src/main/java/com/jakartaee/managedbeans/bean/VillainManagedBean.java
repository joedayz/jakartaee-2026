package com.jakartaee.managedbeans.bean;

import com.jakartaee.common.entities.Villain;
import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Managed Bean para gestionar villanos.
 * 
 * Este bean demuestra cómo múltiples Managed Beans pueden coexistir
 * y ser gestionados independientemente por el contenedor.
 */
@ManagedBean
@ApplicationScoped
public class VillainManagedBean {
    
    private static final Logger logger = Logger.getLogger(VillainManagedBean.class.getName());
    
    @Inject
    EntityManager entityManager;
    
    private String beanName;
    private int operationCount = 0;
    
    public VillainManagedBean() {
        logger.info("🏗️  Constructor de VillainManagedBean llamado");
    }
    
    @PostConstruct
    public void initialize() {
        beanName = "VillainManagedBean-" + System.currentTimeMillis();
        logger.info(String.format("✅ @PostConstruct ejecutado para %s", beanName));
        logger.info("   Managed Bean de villanos inicializado");
    }
    
    @PreDestroy
    public void cleanup() {
        logger.info(String.format("🛑 @PreDestroy ejecutado para %s", beanName));
        logger.info(String.format("   Total de operaciones: %d", operationCount));
    }
    
    @Transactional
    public List<Villain> getAllVillains() {
        operationCount++;
        logger.fine(String.format("[%s] getAllVillains() - Operación #%d", beanName, operationCount));
        return entityManager.createQuery("SELECT v FROM Villain v ORDER BY v.name", Villain.class)
                .getResultList();
    }
    
    @Transactional
    public Optional<Villain> getVillainById(Long id) {
        operationCount++;
        logger.fine(String.format("[%s] getVillainById(%d) - Operación #%d", beanName, id, operationCount));
        return Optional.ofNullable(entityManager.find(Villain.class, id));
    }
    
    @Transactional
    public Villain createVillain(String name, String power, Integer powerLevel) {
        operationCount++;
        logger.fine(String.format("[%s] createVillain(%s) - Operación #%d", beanName, name, operationCount));
        
        Villain villain = new Villain(name, power, powerLevel);
        entityManager.persist(villain);
        entityManager.flush();
        
        return villain;
    }
}

