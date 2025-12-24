package com.jakartaee.nosql.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Hero para MongoDB usando Panache MongoDB.
 * Demuestra características de MongoDB:
 * - Documentos anidados (missions)
 * - Arrays (abilities)
 * - Fechas
 * - ObjectId como identificador
 */
@MongoEntity(collection = "heroes")
public class HeroMongo extends PanacheMongoEntity {
    
    public String name;
    public String power;
    public Integer powerLevel; // 1-100
    public String description;
    public Boolean isActive = true;
    
    /**
     * Array de habilidades (característica de MongoDB).
     */
    public List<String> abilities = new ArrayList<>();
    
    /**
     * Documento anidado para ubicación.
     */
    public Location location;
    
    /**
     * Array de documentos anidados para misiones.
     */
    public List<Mission> missions = new ArrayList<>();
    
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    
    /**
     * Clase anidada para ubicación.
     */
    public static class Location {
        public String city;
        public String planet;
        public Coordinates coordinates;
        
        public static class Coordinates {
            public Double latitude;
            public Double longitude;
        }
    }
    
    /**
     * Clase anidada para misiones.
     */
    public static class Mission {
        public String title;
        public String description;
        public String status; // PENDING, IN_PROGRESS, COMPLETED
        public LocalDateTime createdAt;
        public LocalDateTime completedAt;
    }
    
    // Constructors
    public HeroMongo() {
    }
    
    public HeroMongo(String name, String power, Integer powerLevel) {
        this.name = name;
        this.power = power;
        this.powerLevel = powerLevel;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public void addAbility(String ability) {
        if (abilities == null) {
            abilities = new ArrayList<>();
        }
        abilities.add(ability);
    }
    
    public void addMission(Mission mission) {
        if (missions == null) {
            missions = new ArrayList<>();
        }
        missions.add(mission);
    }
    
    // Getters and Setters
    // El campo 'id' viene heredado de PanacheMongoEntity
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPower() {
        return power;
    }
    
    public void setPower(String power) {
        this.power = power;
    }
    
    public Integer getPowerLevel() {
        return powerLevel;
    }
    
    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public List<String> getAbilities() {
        return abilities;
    }
    
    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }
    
    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public List<Mission> getMissions() {
        return missions;
    }
    
    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

