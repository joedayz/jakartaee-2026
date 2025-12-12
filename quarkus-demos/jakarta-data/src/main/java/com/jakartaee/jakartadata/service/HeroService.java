package com.jakartaee.jakartadata.service;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.jakartadata.repository.HeroRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que demuestra el uso de Jakarta Data Repository.
 */
@ApplicationScoped
public class HeroService {
    
    @Inject
    HeroRepository heroRepository;
    
    /**
     * Crea un nuevo héroe.
     */
    @Transactional
    public void createHero(Hero hero) {
        heroRepository.save(hero);
    }
    
    /**
     * Obtiene un héroe por ID.
     */
    public Optional<Hero> getHeroById(Long id) {
        return heroRepository.findById(id);
    }
    
    /**
     * Obtiene héroes por nombre (búsqueda con patrón).
     */
    public List<Hero> findHeroesByName(String namePattern) {
        return heroRepository.findByName(namePattern);
    }
    
    /**
     * Obtiene todos los héroes activos.
     */
    public List<Hero> getActiveHeroes() {
        return heroRepository.findByIsActiveTrue();
    }
    
    /**
     * Obtiene todos los héroes.
     */
    public List<Hero> getAllHeroes() {
        return heroRepository.findAll();
    }
    
    /**
     * Actualiza un héroe (guardando de nuevo).
     */
    @Transactional
    public void updateHero(Hero hero) {
        heroRepository.save(hero);
    }
    
    /**
     * Elimina un héroe.
     */
    @Transactional
    public void deleteHero(Long id) {
        heroRepository.deleteById(id);
    }
}
