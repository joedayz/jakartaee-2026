package com.jakartaee.nosql.service;

import com.jakartaee.nosql.entity.HeroMongo;
import com.jakartaee.nosql.entity.VillainMongo;
import com.jakartaee.nosql.repository.HeroMongoRepository;
import com.jakartaee.nosql.repository.VillainMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio que demuestra operaciones con MongoDB usando Panache MongoDB.
 * Muestra características específicas de NoSQL:
 * - Documentos anidados
 * - Arrays
 * - Queries flexibles
 * - Operaciones sin esquema fijo
 */
@ApplicationScoped
public class NoSQLService {
    
    @Inject
    HeroMongoRepository heroRepository;
    
    @Inject
    VillainMongoRepository villainRepository;
    
    /**
     * Crea un héroe usando Active Record Pattern de Panache MongoDB.
     */
    public HeroMongo createHero(HeroMongo hero) {
        hero.createdAt = LocalDateTime.now();
        hero.updatedAt = LocalDateTime.now();
        hero.persist(); // Active Record Pattern
        return hero;
    }
    
    /**
     * Crea un héroe usando el repositorio.
     */
    public HeroMongo createHeroWithRepository(HeroMongo hero) {
        hero.createdAt = LocalDateTime.now();
        hero.updatedAt = LocalDateTime.now();
        heroRepository.persist(hero);
        return hero;
    }
    
    /**
     * Obtiene un héroe por ID.
     */
    public Optional<HeroMongo> getHeroById(ObjectId id) {
        return Optional.ofNullable(heroRepository.findById(id));
    }
    
    /**
     * Obtiene todos los héroes.
     */
    public List<HeroMongo> getAllHeroes() {
        return heroRepository.listAll();
    }
    
    /**
     * Busca héroes poderosos.
     */
    public List<HeroMongo> findPowerfulHeroes(int minPowerLevel) {
        return heroRepository.findPowerful(minPowerLevel);
    }
    
    /**
     * Busca héroes activos.
     */
    public List<HeroMongo> findActiveHeroes() {
        return heroRepository.findActive();
    }
    
    /**
     * Busca héroes por nombre (búsqueda parcial).
     */
    public List<HeroMongo> findHeroesByName(String namePattern) {
        return heroRepository.findByNameLike("%" + namePattern + "%");
    }
    
    /**
     * Busca héroes que tengan una habilidad específica.
     * Demuestra queries en arrays de MongoDB.
     */
    public List<HeroMongo> findHeroesByAbility(String ability) {
        return heroRepository.findByAbility(ability);
    }
    
    /**
     * Busca héroes por ciudad.
     * Demuestra queries en documentos anidados.
     */
    public List<HeroMongo> findHeroesByCity(String city) {
        return heroRepository.findByCity(city);
    }
    
    /**
     * Busca héroes con misiones pendientes.
     * Demuestra queries en arrays de documentos anidados.
     */
    public List<HeroMongo> findHeroesWithPendingMissions() {
        return heroRepository.findWithPendingMissions();
    }
    
    /**
     * Agrega una habilidad a un héroe.
     * Demuestra actualización de arrays en MongoDB.
     */
    public HeroMongo addAbilityToHero(ObjectId heroId, String ability) {
        HeroMongo hero = heroRepository.findById(heroId);
        if (hero != null) {
            hero.addAbility(ability);
            hero.updatedAt = LocalDateTime.now();
            hero.update(); // Active Record Pattern
        }
        return hero;
    }
    
    /**
     * Agrega una misión a un héroe.
     * Demuestra actualización de arrays de documentos anidados.
     */
    public HeroMongo addMissionToHero(ObjectId heroId, HeroMongo.Mission mission) {
        HeroMongo hero = heroRepository.findById(heroId);
        if (hero != null) {
            mission.createdAt = LocalDateTime.now();
            hero.addMission(mission);
            hero.updatedAt = LocalDateTime.now();
            hero.update();
        }
        return hero;
    }
    
    /**
     * Actualiza un héroe.
     */
    public HeroMongo updateHero(ObjectId id, HeroMongo heroUpdate) {
        HeroMongo hero = heroRepository.findById(id);
        if (hero != null) {
            hero.name = heroUpdate.name;
            hero.power = heroUpdate.power;
            hero.powerLevel = heroUpdate.powerLevel;
            hero.description = heroUpdate.description;
            hero.updatedAt = LocalDateTime.now();
            hero.update();
        }
        return hero;
    }
    
    /**
     * Elimina un héroe.
     */
    public void deleteHero(ObjectId id) {
        heroRepository.deleteById(id);
    }
    
    /**
     * Crea un villano.
     */
    public VillainMongo createVillain(VillainMongo villain) {
        villain.createdAt = LocalDateTime.now();
        villain.updatedAt = LocalDateTime.now();
        villain.persist();
        return villain;
    }
    
    /**
     * Busca villanos peligrosos.
     */
    public List<VillainMongo> findDangerousVillains() {
        return villainRepository.findDangerous();
    }
    
    /**
     * Busca villanos por nivel de amenaza.
     */
    public List<VillainMongo> findVillainsByThreatLevel(String threatLevel) {
        return villainRepository.findByThreatLevel(threatLevel);
    }
    
    /**
     * Obtiene estadísticas de héroes.
     * Demuestra agregaciones básicas.
     */
    public Map<String, Object> getHeroStatistics() {
        long total = heroRepository.count();
        long active = heroRepository.count("isActive", true);
        long powerful = heroRepository.countByPowerLevel(80);
        
        return Map.of(
            "total", total,
            "active", active,
            "powerful", powerful,
            "inactive", total - active
        );
    }
}

