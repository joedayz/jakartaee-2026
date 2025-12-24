package com.jakartaee.jsonbinding.model;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * Modelo Location que demuestra objetos anidados en JSON-Binding.
 */
public class Location {
    
    @JsonbProperty("city")
    private String city;
    
    @JsonbProperty("country")
    private String country;
    
    @JsonbProperty("coordinates")
    private Coordinates coordinates;
    
    // Constructors
    public Location() {
    }
    
    public Location(String city, String country) {
        this.city = city;
        this.country = country;
    }
    
    // Getters and Setters
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    /**
     * Clase anidada para coordenadas.
     */
    public static class Coordinates {
        private Double latitude;
        private Double longitude;
        
        public Coordinates() {
        }
        
        public Coordinates(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        public Double getLatitude() {
            return latitude;
        }
        
        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }
        
        public Double getLongitude() {
            return longitude;
        }
        
        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
}

