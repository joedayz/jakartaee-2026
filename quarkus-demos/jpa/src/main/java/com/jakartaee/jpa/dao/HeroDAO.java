package com.jakartaee.jpa.dao;

import com.jakartaee.jpa.entity.HeroJPA;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * DAO (Data Access Object) para Heroes usando JPA EntityManager.
 * Demuestra diferentes formas de usar EntityManager:
 * - @Inject EntityManager (CDI)
 * - @PersistenceContext EntityManager (JPA estándar)
 * - Named Queries
 * - JPQL queries
 * - Operaciones CRUD básicas
 */
@ApplicationScoped
public class HeroDAO {
    
    /**
     * Forma 1: Inyección usando @Inject (CDI)
     * Funciona en Quarkus y es la forma recomendada.
     */
    @Inject
    EntityManager entityManager;
    
    /**
     * Forma 2: Inyección usando @PersistenceContext (JPA estándar)
     * También funciona en Quarkus.
     */
    // @PersistenceContext
    // EntityManager entityManager;
    
    /**
     * Crea un nuevo héroe usando EntityManager.persist().
     */
    @Transactional
    public HeroJPA create(HeroJPA hero) {
        entityManager.persist(hero);
        entityManager.flush(); // Forzar sincronización con la BD
        return hero;
    }
    
    /**
     * Busca un héroe por ID usando EntityManager.find().
     */
    public Optional<HeroJPA> findById(Long id) {
        HeroJPA hero = entityManager.find(HeroJPA.class, id);
        return Optional.ofNullable(hero);
    }
    
    /**
     * Busca un héroe por ID con referencia (no carga de la BD).
     */
    public HeroJPA getReference(Long id) {
        return entityManager.getReference(HeroJPA.class, id);
    }
    
    /**
     * Obtiene todos los héroes usando Named Query.
     */
    public List<HeroJPA> findAll() {
        TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findAll", HeroJPA.class);
        return query.getResultList();
    }
    
    /**
     * Busca héroes por nombre usando Named Query.
     */
    public Optional<HeroJPA> findByName(String name) {
        TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findByName", HeroJPA.class);
        query.setParameter("name", name);
        List<HeroJPA> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    /**
     * Busca héroes por nivel de poder usando Named Query.
     */
    public List<HeroJPA> findByPowerLevel(int minLevel) {
        TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findByPowerLevel", HeroJPA.class);
        query.setParameter("minLevel", minLevel);
        return query.getResultList();
    }
    
    /**
     * Busca héroes activos usando Named Query.
     */
    public List<HeroJPA> findActive() {
        TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findActive", HeroJPA.class);
        return query.getResultList();
    }
    
    /**
     * Busca héroes por rango de poder usando Named Query.
     */
    public List<HeroJPA> findByPowerRange(int minLevel, int maxLevel) {
        TypedQuery<HeroJPA> query = entityManager.createNamedQuery("HeroJPA.findByPowerRange", HeroJPA.class);
        query.setParameter("minLevel", minLevel);
        query.setParameter("maxLevel", maxLevel);
        return query.getResultList();
    }
    
    /**
     * Busca héroes usando JPQL query dinámica.
     */
    public List<HeroJPA> findByPowerLevelJPQL(int minLevel) {
        String jpql = "SELECT h FROM HeroJPA h WHERE h.powerLevel >= :minLevel ORDER BY h.powerLevel DESC";
        TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
        query.setParameter("minLevel", minLevel);
        return query.getResultList();
    }
    
    /**
     * Busca héroes por nombre usando JPQL con LIKE.
     */
    public List<HeroJPA> searchByName(String namePattern) {
        String jpql = "SELECT h FROM HeroJPA h WHERE h.name LIKE :pattern ORDER BY h.name";
        TypedQuery<HeroJPA> query = entityManager.createQuery(jpql, HeroJPA.class);
        query.setParameter("pattern", "%" + namePattern + "%");
        return query.getResultList();
    }
    
    /**
     * Cuenta héroes por nivel de poder usando Named Query.
     */
    public long countByPowerLevel(int minLevel) {
        TypedQuery<Long> query = entityManager.createNamedQuery("HeroJPA.countByPowerLevel", Long.class);
        query.setParameter("minLevel", minLevel);
        return query.getSingleResult();
    }
    
    /**
     * Actualiza un héroe usando EntityManager.merge().
     */
    @Transactional
    public HeroJPA update(HeroJPA hero) {
        return entityManager.merge(hero);
    }
    
    /**
     * Actualiza el nivel de poder usando Named Query UPDATE.
     */
    @Transactional
    public int updatePowerLevel(Long id, int newLevel) {
        jakarta.persistence.Query query = entityManager.createNamedQuery("HeroJPA.updatePowerLevel");
        query.setParameter("id", id);
        query.setParameter("newLevel", newLevel);
        return query.executeUpdate();
    }
    
    /**
     * Refresca la entidad desde la base de datos usando EntityManager.refresh().
     */
    @Transactional
    public void refresh(HeroJPA hero) {
        entityManager.refresh(hero);
    }
    
    /**
     * Desvincula la entidad del contexto de persistencia usando EntityManager.detach().
     */
    public void detach(HeroJPA hero) {
        entityManager.detach(hero);
    }
    
    /**
     * Verifica si la entidad está gestionada usando EntityManager.contains().
     */
    public boolean isManaged(HeroJPA hero) {
        return entityManager.contains(hero);
    }
    
    /**
     * Elimina un héroe usando EntityManager.remove().
     */
    @Transactional
    public void delete(Long id) {
        HeroJPA hero = entityManager.find(HeroJPA.class, id);
        if (hero != null) {
            entityManager.remove(hero);
        }
    }
    
    /**
     * Elimina un héroe directamente (debe estar gestionado).
     */
    @Transactional
    public void delete(HeroJPA hero) {
        entityManager.remove(hero);
    }
    
    /**
     * Limpia el contexto de persistencia usando EntityManager.clear().
     */
    @Transactional
    public void clear() {
        entityManager.clear();
    }
    
    /**
     * Fuerza la sincronización con la base de datos usando EntityManager.flush().
     */
    @Transactional
    public void flush() {
        entityManager.flush();
    }
}

