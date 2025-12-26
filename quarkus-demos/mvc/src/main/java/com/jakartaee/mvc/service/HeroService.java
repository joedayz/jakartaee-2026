package com.jakartaee.mvc.service;

import com.jakartaee.common.entities.Hero;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar héroes.
 */
@ApplicationScoped
public class HeroService {
    
    @Inject
    EntityManager entityManager;
    
    public List<Hero> getAllHeroes() {
        return entityManager.createQuery("SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
    }
    
    public Optional<Hero> getHeroById(Long id) {
        return Optional.ofNullable(entityManager.find(Hero.class, id));
    }
    
    @Transactional
    public Hero createHero(String name, String power, Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        entityManager.flush();
        return hero;
    }
    
    @Transactional
    public Hero updateHero(Long id, String name, String power, Integer powerLevel) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            hero.setName(name);
            hero.setPower(power);
            hero.setPowerLevel(powerLevel);
            entityManager.merge(hero);
        }
        return hero;
    }
    
    @Transactional
    public boolean deleteHero(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            entityManager.remove(hero);
            return true;
        }
        return false;
    }
}

