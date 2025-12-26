package com.jakartaee.mvc.service;

import com.jakartaee.common.entities.Villain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar villanos.
 */
@ApplicationScoped
public class VillainService {
    
    @Inject
    EntityManager entityManager;
    
    public List<Villain> getAllVillains() {
        return entityManager.createQuery("SELECT v FROM Villain v ORDER BY v.name", Villain.class)
                .getResultList();
    }
    
    public Optional<Villain> getVillainById(Long id) {
        return Optional.ofNullable(entityManager.find(Villain.class, id));
    }
    
    @Transactional
    public Villain createVillain(String name, String power, Integer powerLevel) {
        Villain villain = new Villain(name, power, powerLevel);
        entityManager.persist(villain);
        entityManager.flush();
        return villain;
    }
    
    @Transactional
    public Villain updateVillain(Long id, String name, String power, Integer powerLevel) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain != null) {
            villain.setName(name);
            villain.setPower(power);
            villain.setPowerLevel(powerLevel);
            entityManager.merge(villain);
        }
        return villain;
    }
    
    @Transactional
    public boolean deleteVillain(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain != null) {
            entityManager.remove(villain);
            return true;
        }
        return false;
    }
}

