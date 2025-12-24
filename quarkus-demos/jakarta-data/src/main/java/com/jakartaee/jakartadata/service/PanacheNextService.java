package com.jakartaee.jakartadata.service;

import com.jakartaee.jakartadata.entity.HeroPanacheEntity;
import com.jakartaee.jakartadata.entity.VillainPanacheEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que demuestra el uso de Panache Next.
 * Muestra:
 * - Active Record Pattern (métodos de instancia)
 * - Repositorios anidados con @HQL
 * - Combinación de ambos enfoques
 */
@ApplicationScoped
public class PanacheNextService {
    
    @Inject
    HeroPanacheEntity.Repo heroRepo;
    
    @Inject
    VillainPanacheEntity.Repo villainRepo;
    
    /**
     * Crea un héroe usando Active Record Pattern.
     * Demuestra cómo usar métodos de instancia directamente.
     */
    @Transactional
    public HeroPanacheEntity createHeroWithActiveRecord(String name, String power, Integer powerLevel) {
        HeroPanacheEntity hero = new HeroPanacheEntity();
        hero.name = name;
        hero.power = power;
        hero.powerLevel = powerLevel;
        hero.isActive = true;
        
        // Active Record: persistir directamente en la instancia
        hero.persist();
        
        return hero;
    }
    
    /**
     * Crea un héroe usando el repositorio anidado.
     * Demuestra cómo usar el repositorio con @HQL.
     */
    @Transactional
    public HeroPanacheEntity createHeroWithRepository(String name, String power, Integer powerLevel) {
        HeroPanacheEntity hero = new HeroPanacheEntity();
        hero.name = name;
        hero.power = power;
        hero.powerLevel = powerLevel;
        hero.isActive = true;
        
        // Usar el repositorio para persistir
        heroRepo.persist(hero);
        
        return hero;
    }
    
    /**
     * Busca héroes poderosos usando @HQL del repositorio anidado.
     */
    public List<HeroPanacheEntity> findPowerfulHeroes(int minLevel) {
        return heroRepo.findPowerful(minLevel);
    }
    
    /**
     * Busca héroes activos usando @HQL.
     */
    public List<HeroPanacheEntity> findActiveHeroes() {
        return heroRepo.findActive();
    }
    
    /**
     * Busca héroes por rango de poder usando @HQL.
     */
    public List<HeroPanacheEntity> findHeroesByPowerRange(int minLevel, int maxLevel) {
        return heroRepo.findByPowerRange(minLevel, maxLevel);
    }
    
    /**
     * Obtiene un héroe por ID usando el repositorio.
     */
    public Optional<HeroPanacheEntity> getHeroById(Long id) {
        HeroPanacheEntity hero = heroRepo.findById(id);
        return Optional.ofNullable(hero);
    }
    
    /**
     * Actualiza un héroe usando Active Record Pattern.
     * Demuestra cómo los cambios se persisten automáticamente.
     */
    @Transactional
    public HeroPanacheEntity updateHeroWithActiveRecord(Long id, String newName) {
        HeroPanacheEntity hero = heroRepo.findById(id);
        if (hero == null) {
            throw new IllegalArgumentException("Hero not found with id: " + id);
        }
        
        hero.name = newName;
        // En managed entities, los cambios se persisten automáticamente
        // No necesitamos llamar a update() explícitamente
        
        return hero;
    }
    
    /**
     * Elimina un héroe usando Active Record Pattern.
     */
    @Transactional
    public void deleteHeroWithActiveRecord(Long id) {
        HeroPanacheEntity hero = heroRepo.findById(id);
        if (hero != null) {
            hero.delete(); // Active Record: eliminar directamente
        }
    }
    
    /**
     * Elimina un héroe por nombre usando @HQL del repositorio.
     */
    @Transactional
    public long deleteHeroByName(String name) {
        return heroRepo.deleteByName(name);
    }
    
    /**
     * Cuenta héroes activos usando @HQL.
     */
    public long countActiveHeroes() {
        return heroRepo.countActive();
    }
    
    /**
     * Busca villanos peligrosos usando @HQL.
     */
    public List<VillainPanacheEntity> findDangerousVillains() {
        return villainRepo.findDangerous();
    }
    
    /**
     * Busca villanos por nivel de amenaza usando @HQL.
     */
    public List<VillainPanacheEntity> findVillainsByThreatLevel(String threatLevel) {
        return villainRepo.findByThreatLevel(threatLevel);
    }
    
    /**
     * Obtiene todos los héroes ordenados por poder usando @HQL.
     */
    public List<HeroPanacheEntity> getAllHeroesOrderedByPower() {
        return heroRepo.findAllOrderedByPower();
    }
}

