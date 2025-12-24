package com.jakartaee.nosql.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Villain para MongoDB usando Panache MongoDB.
 * Demuestra características específicas de MongoDB.
 */
@MongoEntity(collection = "villains")
public class VillainMongo extends PanacheMongoEntity {
    
    public String name;
    public String power;
    public Integer powerLevel;
    public String description;
    public String threatLevel; // LOW, MEDIUM, HIGH, CRITICAL
    public Boolean isActive = true;
    
    /**
     * Array de habilidades.
     */
    public List<String> abilities = new ArrayList<>();
    
    /**
     * Array de aliados (referencias a otros villanos).
     */
    public List<String> allies = new ArrayList<>();
    
    /**
     * Documento anidado para ubicación de la base secreta.
     */
    public SecretBase secretBase;
    
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    
    /**
     * Clase anidada para base secreta.
     */
    public static class SecretBase {
        public String name;
        public String location;
        public Coordinates coordinates;
        public List<String> features = new ArrayList<>();
        
        public static class Coordinates {
            public Double latitude;
            public Double longitude;
        }
    }
    
    // Constructors
    public VillainMongo() {
    }
    
    public VillainMongo(String name, String power, Integer powerLevel) {
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
    
    public void addAlly(String allyName) {
        if (allies == null) {
            allies = new ArrayList<>();
        }
        allies.add(allyName);
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
    
    public String getThreatLevel() {
        return threatLevel;
    }
    
    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
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
    
    public List<String> getAllies() {
        return allies;
    }
    
    public void setAllies(List<String> allies) {
        this.allies = allies;
    }
    
    public SecretBase getSecretBase() {
        return secretBase;
    }
    
    public void setSecretBase(SecretBase secretBase) {
        this.secretBase = secretBase;
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

