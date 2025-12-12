package com.jakartaee.beanvalidation.service;

import com.jakartaee.beanvalidation.dto.HeroCreateDTO;
import com.jakartaee.beanvalidation.dto.HeroUpdateDTO;
import com.jakartaee.common.entities.Hero;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Servicio que demuestra validaciones programáticas con Bean Validation.
 * Muestra cómo usar el Validator para validar objetos manualmente.
 */
@ApplicationScoped
public class HeroService {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    Validator validator;
    
    /**
     * Crea un nuevo héroe con validación programática.
     * Demuestra cómo validar manualmente usando Validator.
     */
    @Transactional
    public Hero createHero(HeroCreateDTO dto) {
        // Validación programática usando Validator
        Set<ConstraintViolation<HeroCreateDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        
        // Convertir DTO a entidad
        Hero hero = new Hero();
        hero.setName(dto.name());
        hero.setPower(dto.power());
        hero.setPowerLevel(dto.powerLevel());
        hero.setDescription(dto.description());
        
        // Validar la entidad antes de persistir
        Set<ConstraintViolation<Hero>> entityViolations = validator.validate(hero);
        if (!entityViolations.isEmpty()) {
            throw new ConstraintViolationException(entityViolations);
        }
        
        entityManager.persist(hero);
        return hero;
    }
    
    /**
     * Actualiza un héroe existente con validación programática.
     */
    @Transactional
    public Hero updateHero(HeroUpdateDTO dto) {
        // Validación programática
        Set<ConstraintViolation<HeroUpdateDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        
        Hero hero = entityManager.find(Hero.class, dto.id());
        if (hero == null) {
            throw new IllegalArgumentException("Hero not found with id: " + dto.id());
        }
        
        hero.setName(dto.name());
        hero.setPower(dto.power());
        hero.setPowerLevel(dto.powerLevel());
        hero.setDescription(dto.description());
        
        // Validar la entidad actualizada
        Set<ConstraintViolation<Hero>> entityViolations = validator.validate(hero);
        if (!entityViolations.isEmpty()) {
            throw new ConstraintViolationException(entityViolations);
        }
        
        return hero;
    }
    
    /**
     * Obtiene un héroe por ID.
     */
    public Optional<Hero> getHeroById(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        return Optional.ofNullable(hero);
    }
    
    /**
     * Obtiene todos los héroes.
     */
    public List<Hero> getAllHeroes() {
        return entityManager.createQuery("SELECT h FROM Hero h ORDER BY h.id", Hero.class)
                .getResultList();
    }
    
    /**
     * Busca héroes por nombre (búsqueda parcial).
     */
    public List<Hero> findHeroesByName(String namePattern) {
        return entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.name LIKE :pattern ORDER BY h.name", 
                Hero.class)
                .setParameter("pattern", "%" + namePattern + "%")
                .getResultList();
    }
    
    /**
     * Obtiene todos los héroes activos.
     */
    public List<Hero> getActiveHeroes() {
        return entityManager.createQuery(
                "SELECT h FROM Hero h WHERE h.isActive = true ORDER BY h.name", 
                Hero.class)
                .getResultList();
    }
    
    /**
     * Elimina un héroe.
     */
    @Transactional
    public void deleteHero(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            entityManager.remove(hero);
        }
    }
}

