package com.jakartaee.jsonbinding.model;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo Team que demuestra características avanzadas de JSON-Binding:
 * - @JsonbProperty para renombrar campos
 * - @JsonbDateFormat para formatear fechas
 * - @JsonbTransient para excluir campos
 * - Objetos anidados
 * - Colecciones
 */
public class Team {
    
    @JsonbProperty("team_id")
    private Long id;
    
    @JsonbProperty("team_name")
    private String name;
    
    private String description;
    
    @JsonbProperty("formation_date")
    @JsonbDateFormat("yyyy-MM-dd")
    private LocalDate formationDate;
    
    @JsonbProperty("members")
    private List<TeamMember> members = new ArrayList<>();
    
    @JsonbProperty("headquarters")
    private Location headquarters;
    
    @JsonbProperty("active")
    private Boolean isActive = true;
    
    @JsonbTransient
    private String internalNotes; // No se serializa
    
    // Constructors
    public Team() {
    }
    
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
        this.formationDate = LocalDate.now();
    }
    
    // Helper methods
    public void addMember(TeamMember member) {
        members.add(member);
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getFormationDate() {
        return formationDate;
    }
    
    public void setFormationDate(LocalDate formationDate) {
        this.formationDate = formationDate;
    }
    
    public List<TeamMember> getMembers() {
        return members;
    }
    
    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }
    
    public Location getHeadquarters() {
        return headquarters;
    }
    
    public void setHeadquarters(Location headquarters) {
        this.headquarters = headquarters;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getInternalNotes() {
        return internalNotes;
    }
    
    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }
    
    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", membersCount=" + (members != null ? members.size() : 0) +
                '}';
    }
}

