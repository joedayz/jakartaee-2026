package com.jakartaee.jsonbinding.model;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;

/**
 * Modelo TeamMember que demuestra:
 * - @JsonbProperty para renombrar campos
 * - @JsonbPropertyOrder para ordenar campos en JSON
 */
@JsonbPropertyOrder({"role", "hero_name", "power_level"})
public class TeamMember {
    
    @JsonbProperty("hero_name")
    private String heroName;
    
    @JsonbProperty("power_level")
    private Integer powerLevel;
    
    private String role;
    
    @JsonbProperty("special_abilities")
    private List<String> specialAbilities = new java.util.ArrayList<>();
    
    // Constructors
    public TeamMember() {
    }
    
    public TeamMember(String heroName, Integer powerLevel, String role) {
        this.heroName = heroName;
        this.powerLevel = powerLevel;
        this.role = role;
    }
    
    // Getters and Setters
    public String getHeroName() {
        return heroName;
    }
    
    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }
    
    public Integer getPowerLevel() {
        return powerLevel;
    }
    
    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public List<String> getSpecialAbilities() {
        return specialAbilities;
    }
    
    public void setSpecialAbilities(List<String> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }
    
    public void addSpecialAbility(String ability) {
        specialAbilities.add(ability);
    }
}

