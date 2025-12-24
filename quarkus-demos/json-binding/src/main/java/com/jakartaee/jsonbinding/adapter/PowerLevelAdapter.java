package com.jakartaee.jsonbinding.adapter;

import jakarta.json.bind.adapter.JsonbAdapter;

/**
 * Custom Adapter para convertir PowerLevel entre diferentes representaciones.
 * Demuestra cómo crear adapters personalizados en JSON-Binding.
 */
public class PowerLevelAdapter implements JsonbAdapter<Integer, PowerLevelAdapter.PowerLevelDTO> {
    
    @Override
    public PowerLevelDTO adaptToJson(Integer powerLevel) throws Exception {
        PowerLevelDTO dto = new PowerLevelDTO();
        dto.value = powerLevel;
        dto.category = categorizePowerLevel(powerLevel);
        dto.description = getPowerDescription(powerLevel);
        return dto;
    }
    
    @Override
    public Integer adaptFromJson(PowerLevelDTO dto) throws Exception {
        return dto.value;
    }
    
    private String categorizePowerLevel(Integer powerLevel) {
        if (powerLevel >= 90) return "LEGENDARY";
        if (powerLevel >= 80) return "VERY_HIGH";
        if (powerLevel >= 70) return "HIGH";
        if (powerLevel >= 60) return "MEDIUM";
        return "LOW";
    }
    
    private String getPowerDescription(Integer powerLevel) {
        if (powerLevel >= 90) return "Legendary hero with cosmic powers";
        if (powerLevel >= 80) return "Very powerful hero";
        if (powerLevel >= 70) return "Powerful hero";
        if (powerLevel >= 60) return "Moderately powerful hero";
        return "Standard hero";
    }
    
    /**
     * DTO interno para la representación JSON del nivel de poder.
     */
    public static class PowerLevelDTO {
        public Integer value;
        public String category;
        public String description;
    }
}

