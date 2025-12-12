package com.jakartaee.cdi.service;

import com.jakartaee.cdi.event.VillainCreatedEvent;
import com.jakartaee.cdi.interceptor.Loggable;
import com.jakartaee.cdi.qualifier.VillainQualifier;
import com.jakartaee.cdi.stereotype.Service;
import com.jakartaee.common.entities.Villain;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar Villains usando CDI.
 * Demuestra:
 * - @Service stereotype
 * - @VillainQualifier para identificar el servicio
 * - Inyección de EntityManager
 * - Eventos CDI
 */
@Service
@VillainQualifier
public class VillainService {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    Event<VillainCreatedEvent> villainCreatedEvent;
    
    /**
     * Crea un nuevo villano y dispara un evento.
     */
    @Transactional
    @Loggable("INFO")
    public Villain createVillain(Villain villain) {
        entityManager.persist(villain);
        entityManager.flush();
        
        // Disparar evento CDI
        villainCreatedEvent.fire(new VillainCreatedEvent(villain));
        
        return villain;
    }
    
    /**
     * Obtiene un villano por ID.
     */
    @Loggable("DEBUG")
    public Optional<Villain> getVillainById(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        return Optional.ofNullable(villain);
    }
    
    /**
     * Obtiene todos los villanos.
     */
    @Loggable("DEBUG")
    public List<Villain> getAllVillains() {
        return entityManager.createQuery("SELECT v FROM Villain v ORDER BY v.id", Villain.class)
                .getResultList();
    }
    
    /**
     * Busca villanos por nombre.
     */
    @Loggable("DEBUG")
    public List<Villain> findVillainsByName(String namePattern) {
        return entityManager.createQuery(
                "SELECT v FROM Villain v WHERE v.name LIKE :pattern ORDER BY v.name", 
                Villain.class)
                .setParameter("pattern", "%" + namePattern + "%")
                .getResultList();
    }
    
    /**
     * Obtiene villanos peligrosos (threatLevel = HIGH o CRITICAL).
     */
    @Loggable("WARNING")
    public List<Villain> getDangerousVillains() {
        return entityManager.createQuery(
                "SELECT v FROM Villain v WHERE v.threatLevel IN ('HIGH', 'CRITICAL') ORDER BY v.threatLevel DESC", 
                Villain.class)
                .getResultList();
    }
    
    /**
     * Actualiza un villano.
     */
    @Transactional
    @Loggable("INFO")
    public Villain updateVillain(Villain villain) {
        return entityManager.merge(villain);
    }
    
    /**
     * Elimina un villano.
     */
    @Transactional
    @Loggable("WARNING")
    public void deleteVillain(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain != null) {
            entityManager.remove(villain);
        }
    }
}

