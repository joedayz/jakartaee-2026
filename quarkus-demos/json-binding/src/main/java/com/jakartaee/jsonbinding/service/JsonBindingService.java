package com.jakartaee.jsonbinding.service;

import com.jakartaee.jsonbinding.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyOrderStrategy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que demuestra diferentes características de JSON-Binding:
 * - Serialización/deserialización básica
 * - Configuración personalizada de Jsonb
 * - Uso de anotaciones (@JsonbProperty, @JsonbTransient, etc.)
 * - Custom adapters
 */
@ApplicationScoped
public class JsonBindingService {
    
    /**
     * Serializa un objeto Team a JSON usando configuración por defecto.
     */
    public String serializeTeam(Team team) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(team);
    }
    
    /**
     * Deserializa JSON a un objeto Team.
     */
    public Team deserializeTeam(String json) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(json, Team.class);
    }
    
    /**
     * Serializa con configuración personalizada (orden alfabético).
     */
    public String serializeWithCustomConfig(Object obj) {
        JsonbConfig config = new JsonbConfig()
                .withPropertyOrderStrategy(PropertyOrderStrategy.LEXICOGRAPHICAL)
                .withNullValues(true)
                .withFormatting(true);
        
        Jsonb jsonb = JsonbBuilder.create(config);
        return jsonb.toJson(obj);
    }
    
    /**
     * Crea un Team de ejemplo con todas las características.
     */
    public Team createExampleTeam() {
        Team justiceLeague = new Team("Justice League", "El equipo más poderoso de la Tierra");
        justiceLeague.setId(1L);
        justiceLeague.setFormationDate(LocalDate.of(1960, 2, 1));
        justiceLeague.setIsActive(true);
        justiceLeague.setInternalNotes("Notas internas que no aparecen en JSON");
        
        // Crear ubicación
        Location hq = new Location("Washington D.C.", "USA");
        Location.Coordinates coords = new Location.Coordinates(38.9072, -77.0369);
        hq.setCoordinates(coords);
        justiceLeague.setHeadquarters(hq);
        
        // Agregar miembros
        TeamMember superman = new TeamMember("Superman", 95, "Leader");
        superman.addSpecialAbility("Super strength");
        superman.addSpecialAbility("Flight");
        superman.addSpecialAbility("Heat vision");
        justiceLeague.addMember(superman);
        
        TeamMember batman = new TeamMember("Batman", 85, "Strategist");
        batman.addSpecialAbility("Intelligence");
        batman.addSpecialAbility("Martial arts");
        batman.addSpecialAbility("Technology");
        justiceLeague.addMember(batman);
        
        TeamMember wonderWoman = new TeamMember("Wonder Woman", 90, "Warrior");
        wonderWoman.addSpecialAbility("Super strength");
        wonderWoman.addSpecialAbility("Flight");
        wonderWoman.addSpecialAbility("Lasso of truth");
        justiceLeague.addMember(wonderWoman);
        
        TeamMember flash = new TeamMember("Flash", 88, "Speedster");
        flash.addSpecialAbility("Super speed");
        flash.addSpecialAbility("Time travel");
        justiceLeague.addMember(flash);
        
        return justiceLeague;
    }
    
    /**
     * Crea múltiples teams de ejemplo.
     */
    public List<Team> createExampleTeams() {
        List<Team> teams = new ArrayList<>();
        
        // Justice League
        teams.add(createExampleTeam());
        
        // Teen Titans
        Team teenTitans = new Team("Teen Titans", "Equipo de jóvenes héroes");
        teenTitans.setId(2L);
        teenTitans.setFormationDate(LocalDate.of(1980, 7, 1));
        teenTitans.setIsActive(true);
        
        Location titansHq = new Location("Jump City", "USA");
        titansHq.setCoordinates(new Location.Coordinates(37.7749, -122.4194));
        teenTitans.setHeadquarters(titansHq);
        
        teenTitans.addMember(new TeamMember("Robin", 75, "Leader"));
        teenTitans.addMember(new TeamMember("Starfire", 82, "Warrior"));
        teenTitans.addMember(new TeamMember("Beast Boy", 70, "Shapeshifter"));
        
        teams.add(teenTitans);
        
        return teams;
    }
    
    /**
     * Serializa una lista de teams.
     */
    public String serializeTeams(List<Team> teams) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(teams);
    }
    
    /**
     * Deserializa una lista de teams.
     */
    public List<Team> deserializeTeams(String json) {
        Jsonb jsonb = JsonbBuilder.create();
        jakarta.json.bind.type.Type<List<Team>> listType = new jakarta.json.bind.type.Type<List<Team>>() {};
        return jsonb.fromJson(json, listType);
    }
    
    /**
     * Crea un héroe con custom adapter.
     */
    public HeroWithCustomAdapter createHeroWithAdapter() {
        HeroWithCustomAdapter hero = new HeroWithCustomAdapter();
        hero.setName("Superman");
        hero.setPower("Super strength, flight, heat vision");
        hero.setPowerLevel(95);
        hero.setDescription("El último hijo de Krypton");
        return hero;
    }
    
    /**
     * Serializa un héroe con custom adapter.
     */
    public String serializeHeroWithAdapter(HeroWithCustomAdapter hero) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(hero);
    }
    
    /**
     * Deserializa un héroe con custom adapter.
     */
    public HeroWithCustomAdapter deserializeHeroWithAdapter(String json) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(json, HeroWithCustomAdapter.class);
    }
}

