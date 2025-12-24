package com.jakartaee.jpa.service;

import com.jakartaee.jpa.dao.HeroDAO;
import com.jakartaee.jpa.dao.MissionDAO;
import com.jakartaee.jpa.entity.HeroJPA;
import com.jakartaee.jpa.entity.Mission;
import com.jakartaee.jpa.entity.Mission.MissionStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que demuestra características avanzadas de JPA:
 * - Criteria API para queries type-safe
 * - JPQL queries complejas
 * - Operaciones con relaciones
 * - Transacciones
 */
@ApplicationScoped
public class HeroService {
    
    @Inject
    HeroDAO heroDAO;
    
    @Inject
    MissionDAO missionDAO;
    
    @Inject
    EntityManager entityManager;
    
    /**
     * Crea un nuevo héroe.
     */
    @Transactional
    public HeroJPA createHero(HeroJPA hero) {
        return heroDAO.create(hero);
    }
    
    /**
     * Obtiene un héroe por ID.
     */
    public Optional<HeroJPA> getHeroById(Long id) {
        return heroDAO.findById(id);
    }
    
    /**
     * Obtiene todos los héroes.
     */
    public List<HeroJPA> getAllHeroes() {
        return heroDAO.findAll();
    }
    
    /**
     * Busca héroes por nombre.
     */
    public Optional<HeroJPA> findHeroByName(String name) {
        return heroDAO.findByName(name);
    }
    
    /**
     * Busca héroes poderosos usando Named Query.
     */
    public List<HeroJPA> findPowerfulHeroes(int minLevel) {
        return heroDAO.findByPowerLevel(minLevel);
    }
    
    /**
     * Busca héroes por rango de poder.
     */
    public List<HeroJPA> findHeroesByPowerRange(int minLevel, int maxLevel) {
        return heroDAO.findByPowerRange(minLevel, maxLevel);
    }
    
    /**
     * Busca héroes usando Criteria API.
     * Demuestra cómo construir queries type-safe programáticamente.
     */
    public List<HeroJPA> findHeroesWithCriteria(int minPowerLevel, boolean activeOnly) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<HeroJPA> query = cb.createQuery(HeroJPA.class);
        Root<HeroJPA> hero = query.from(HeroJPA.class);
        
        // Construir predicados dinámicamente
        Predicate powerPredicate = cb.greaterThanOrEqualTo(hero.get("powerLevel"), minPowerLevel);
        
        if (activeOnly) {
            Predicate activePredicate = cb.equal(hero.get("isActive"), true);
            query.where(cb.and(powerPredicate, activePredicate));
        } else {
            query.where(powerPredicate);
        }
        
        // Ordenar por nivel de poder descendente
        query.orderBy(cb.desc(hero.get("powerLevel")));
        
        TypedQuery<HeroJPA> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    
    /**
     * Busca héroes usando Criteria API con múltiples condiciones.
     */
    public List<HeroJPA> findHeroesWithMultipleCriteria(String namePattern, int minPowerLevel, int maxPowerLevel) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<HeroJPA> query = cb.createQuery(HeroJPA.class);
        Root<HeroJPA> hero = query.from(HeroJPA.class);
        
        List<Predicate> predicates = new java.util.ArrayList<>();
        
        if (namePattern != null && !namePattern.isBlank()) {
            predicates.add(cb.like(cb.lower(hero.get("name")), "%" + namePattern.toLowerCase() + "%"));
        }
        
        if (minPowerLevel > 0) {
            predicates.add(cb.greaterThanOrEqualTo(hero.get("powerLevel"), minPowerLevel));
        }
        
        if (maxPowerLevel < 100) {
            predicates.add(cb.lessThanOrEqualTo(hero.get("powerLevel"), maxPowerLevel));
        }
        
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        query.orderBy(cb.desc(hero.get("powerLevel")));
        
        TypedQuery<HeroJPA> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
    
    /**
     * Cuenta héroes usando Criteria API.
     */
    public long countHeroesWithCriteria(int minPowerLevel) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<HeroJPA> hero = query.from(HeroJPA.class);
        
        query.select(cb.count(hero));
        query.where(cb.greaterThanOrEqualTo(hero.get("powerLevel"), minPowerLevel));
        
        return entityManager.createQuery(query).getSingleResult();
    }
    
    /**
     * Busca héroes con sus misiones usando JPQL con JOIN FETCH.
     * Demuestra cómo evitar el problema N+1 con eager loading.
     */
    public List<HeroJPA> findHeroesWithMissions() {
        String jpql = "SELECT DISTINCT h FROM HeroJPA h LEFT JOIN FETCH h.missions ORDER BY h.name";
        TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
        return query.getResultList();
    }
    
    /**
     * Obtiene estadísticas de héroes usando JPQL con funciones de agregación.
     */
    public Map<String, Object> getHeroStatistics() {
        String jpql = "SELECT " +
                "COUNT(h) as total, " +
                "AVG(h.powerLevel) as avgPower, " +
                "MIN(h.powerLevel) as minPower, " +
                "MAX(h.powerLevel) as maxPower, " +
                "SUM(CASE WHEN h.isActive = true THEN 1 ELSE 0 END) as activeCount " +
                "FROM HeroJPA h";
        
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        Object[] results = query.getSingleResult();
        
        return Map.of(
            "total", results[0],
            "avgPower", results[1] != null ? ((Number) results[1]).doubleValue() : 0.0,
            "minPower", results[2],
            "maxPower", results[3],
            "activeCount", results[4]
        );
    }
    
    /**
     * Actualiza un héroe.
     */
    @Transactional
    public HeroJPA updateHero(HeroJPA hero) {
        return heroDAO.update(hero);
    }
    
    /**
     * Actualiza el nivel de poder usando Named Query UPDATE.
     */
    @Transactional
    public int updatePowerLevel(Long id, int newLevel) {
        return heroDAO.updatePowerLevel(id, newLevel);
    }
    
    /**
     * Elimina un héroe.
     */
    @Transactional
    public void deleteHero(Long id) {
        heroDAO.delete(id);
    }
    
    /**
     * Crea una misión para un héroe.
     * Demuestra operaciones con relaciones.
     */
    @Transactional
    public Mission createMission(Long heroId, String title, String description) {
        Optional<HeroJPA> heroOpt = heroDAO.findById(heroId);
        if (heroOpt.isEmpty()) {
            throw new IllegalArgumentException("Hero not found with id: " + heroId);
        }
        
        HeroJPA hero = heroOpt.get();
        Mission mission = new Mission(title, description, hero);
        mission.setStatus(MissionStatus.PENDING);
        
        // Agregar la misión al héroe (manejo bidireccional)
        hero.addMission(mission);
        
        return missionDAO.create(mission);
    }
    
    /**
     * Obtiene todas las misiones de un héroe.
     */
    public List<Mission> getHeroMissions(Long heroId) {
        return missionDAO.findByHeroId(heroId);
    }
    
    /**
     * Completa una misión.
     */
    @Transactional
    public Mission completeMission(Long missionId) {
        return missionDAO.completeMission(missionId);
    }
    
    /**
     * Obtiene estadísticas de misiones por héroe usando JPQL con GROUP BY.
     */
    public List<Map<String, Object>> getMissionStatisticsByHero() {
        String jpql = "SELECT h.id, h.name, COUNT(m) as missionCount, " +
                "SUM(CASE WHEN m.completed = true THEN 1 ELSE 0 END) as completedCount " +
                "FROM HeroJPA h LEFT JOIN h.missions m " +
                "GROUP BY h.id, h.name " +
                "ORDER BY missionCount DESC";
        
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> results = query.getResultList();
        
        return results.stream()
                .map(row -> Map.<String, Object>of(
                    "heroId", row[0],
                    "heroName", row[1],
                    "missionCount", row[2],
                    "completedCount", row[3]
                ))
                .collect(Collectors.toList());
    }
}

