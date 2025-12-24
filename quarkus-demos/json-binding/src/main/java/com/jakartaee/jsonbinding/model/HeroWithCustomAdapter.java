package com.jakartaee.jsonbinding.model;

import jakarta.json.bind.annotation.JsonbTypeAdapter;
import com.jakartaee.jsonbinding.adapter.PowerLevelAdapter;

/**
 * Modelo Hero que demuestra el uso de Custom Adapters en JSON-Binding.
 * El nivel de poder se serializa como un objeto complejo usando PowerLevelAdapter.
 */
public class HeroWithCustomAdapter {
    
    private String name;
    private String power;
    
    @JsonbTypeAdapter(PowerLevelAdapter.class)
    private Integer powerLevel;
    
    private String description;
    
    // Constructors
    public HeroWithCustomAdapter() {
    }
    
    public HeroWithCustomAdapter(String name, String power, Integer powerLevel) {
        this.name = name;
        this.power = power;
        this.powerLevel = powerLevel;
    }
    
    // Getters and Setters
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
}

