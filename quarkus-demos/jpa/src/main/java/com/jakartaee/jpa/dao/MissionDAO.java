package com.jakartaee.jpa.dao;

import com.jakartaee.jpa.entity.Mission;
import com.jakartaee.jpa.entity.Mission.MissionStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * DAO para Missions usando JPA EntityManager.
 * Demuestra:
 * - Named Queries
 * - Relaciones ManyToOne con HeroJPA
 * - Operaciones con relaciones
 */
@ApplicationScoped
public class MissionDAO {
    
    @Inject
    EntityManager entityManager;
    
    /**
     * Crea una nueva misión.
     */
    @Transactional
    public Mission create(Mission mission) {
        entityManager.persist(mission);
        entityManager.flush();
        return mission;
    }
    
    /**
     * Busca una misión por ID.
     */
    public Optional<Mission> findById(Long id) {
        Mission mission = entityManager.find(Mission.class, id);
        return Optional.ofNullable(mission);
    }
    
    /**
     * Busca misiones por ID del héroe usando Named Query.
     */
    public List<Mission> findByHeroId(Long heroId) {
        TypedQuery<Mission> query = entityManager.createNamedQuery("Mission.findByHeroId", Mission.class);
        query.setParameter("heroId", heroId);
        return query.getResultList();
    }
    
    /**
     * Busca misiones completadas usando Named Query.
     */
    public List<Mission> findCompleted() {
        TypedQuery<Mission> query = entityManager.createNamedQuery("Mission.findCompleted", Mission.class);
        return query.getResultList();
    }
    
    /**
     * Busca misiones por estado usando Named Query.
     */
    public List<Mission> findByStatus(MissionStatus status) {
        TypedQuery<Mission> query = entityManager.createNamedQuery("Mission.findByStatus", Mission.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    /**
     * Cuenta misiones por ID del héroe usando Named Query.
     */
    public long countByHeroId(Long heroId) {
        TypedQuery<Long> query = entityManager.createNamedQuery("Mission.countByHeroId", Long.class);
        query.setParameter("heroId", heroId);
        return query.getSingleResult();
    }
    
    /**
     * Busca misiones usando JPQL con JOIN.
     */
    public List<Mission> findMissionsWithHeroes() {
        String jpql = "SELECT m FROM Mission m JOIN FETCH m.hero ORDER BY m.createdAt DESC";
        TypedQuery<Mission> query = entityManager.createQuery(jpql, Mission.class);
        return query.getResultList();
    }
    
    /**
     * Actualiza una misión.
     */
    @Transactional
    public Mission update(Mission mission) {
        return entityManager.merge(mission);
    }
    
    /**
     * Elimina una misión.
     */
    @Transactional
    public void delete(Long id) {
        Mission mission = entityManager.find(Mission.class, id);
        if (mission != null) {
            entityManager.remove(mission);
        }
    }
    
    /**
     * Completa una misión.
     */
    @Transactional
    public Mission completeMission(Long id) {
        Mission mission = entityManager.find(Mission.class, id);
        if (mission != null) {
            mission.setCompleted(true);
            mission.setStatus(MissionStatus.COMPLETED);
            return entityManager.merge(mission);
        }
        return null;
    }
}

