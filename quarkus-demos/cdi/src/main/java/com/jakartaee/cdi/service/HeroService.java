package com.jakartaee.cdi.service;

import com.jakartaee.cdi.event.HeroCreatedEvent;
import com.jakartaee.cdi.interceptor.Loggable;
import com.jakartaee.cdi.qualifier.HeroQualifier;
import com.jakartaee.cdi.stereotype.Service;
import com.jakartaee.common.entities.Hero;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar Heroes usando CDI.
 * Demuestra:
 * - @Service stereotype (agrupa @ApplicationScoped + @Loggable)
 * - @HeroQualifier para identificar el servicio
 * - Inyección de EntityManager
 * - Eventos CDI (@Inject Event)
 */
@Service
@HeroQualifier
public class HeroService {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    Event<HeroCreatedEvent> heroCreatedEvent;
    
    /**
     * Crea un nuevo héroe y dispara un evento.
     */
    @Transactional
    @Loggable("INFO")
    public Hero createHero(Hero hero) {
        entityManager.persist(hero);
        entityManager.flush();
        
        // Disparar evento CDI
        heroCreatedEvent.fire(new HeroCreatedEvent(hero));
        
        return hero;
    }
    
    /**
     * Obtiene un héroe por ID.
     */
    @Loggable("DEBUG")
    public Optional<Hero> getHeroById(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        return Optional.ofNullable(hero);
    }
    
    /**
     * Obtiene todos los héroes.
     */
    @Loggable("DEBUG")
    public List<Hero> getAllHeroes() {
        return entityManager.createQuery("SELECT h FROM Hero h ORDER BY h.id", Hero.class)
                .getResultList();
    }
    
    /**
     * Busca héroes por nombre.
     */
    @Loggable("DEBUG")
    public List<Hero> findHeroesByName(String namePattern) {
        return entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.name LIKE :pattern ORDER BY h.name", 
                Hero.class)
                .setParameter("pattern", "%" + namePattern + "%")
                .getResultList();
    }
    
    /**
     * Obtiene héroes poderosos (powerLevel >= 80).
     */
    @Loggable("INFO")
    public List<Hero> getPowerfulHeroes() {
        return entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.powerLevel >= 80 ORDER BY h.powerLevel DESC", 
                Hero.class)
                .getResultList();
    }
    
    /**
     * Actualiza un héroe.
     */
    @Transactional
    @Loggable("INFO")
    public Hero updateHero(Hero hero) {
        return entityManager.merge(hero);
    }
    
    /**
     * Elimina un héroe.
     */
    @Transactional
    @Loggable("WARNING")
    public void deleteHero(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            entityManager.remove(hero);
        }
    }
}

