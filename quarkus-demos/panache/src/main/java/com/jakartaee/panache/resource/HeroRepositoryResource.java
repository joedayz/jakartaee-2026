package com.jakartaee.panache.resource;

import com.jakartaee.panache.entity.HeroEntity;
import com.jakartaee.panache.repository.HeroRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

/**
 * Recurso REST que demuestra el uso de Panache Repository Pattern.
 * Usa HeroRepository para todas las operaciones.
 */
@Path("/api/heroes/repository")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeroRepositoryResource {
    
    @Inject
    HeroRepository heroRepository;
    
    /**
     * Listar todos los héroes usando Repository Pattern
     */
    @GET
    public Response getAllHeroes() {
        List<HeroEntity> heroes = heroRepository.listAll();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtener héroe por ID usando Repository Pattern
     */
    @GET
    @Path("/{id}")
    public Response getHeroById(@PathParam("id") Long id) {
        return heroRepository.findByIdOptional(id)
                .map(hero -> Response.ok(hero).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Héroe no encontrado con ID: " + id))
                        .build());
    }
    
    /**
     * Buscar héroes poderosos usando método personalizado del repositorio
     */
    @GET
    @Path("/powerful")
    public Response getPowerfulHeroes() {
        List<HeroEntity> heroes = heroRepository.findPowerfulHeroes();
        return Response.ok(Map.of(
            "heroes", heroes,
            "count", heroes.size(),
            "pattern", "Repository Pattern",
            "note", "Usando método personalizado findPowerfulHeroes() del repositorio"
        )).build();
    }
    
    /**
     * Buscar héroes activos
     */
    @GET
    @Path("/active")
    public Response getActiveHeroes() {
        List<HeroEntity> heroes = heroRepository.findActiveHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Buscar héroe por nombre
     */
    @GET
    @Path("/search")
    public Response searchHeroByName(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El parámetro 'name' es requerido"))
                    .build();
        }
        
        return heroRepository.findByNameIgnoreCase(name)
                .map(hero -> Response.ok(hero).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Héroe no encontrado: " + name))
                        .build());
    }
    
    /**
     * Crear héroe usando Repository Pattern
     */
    @POST
    @Transactional
    public Response createHero(HeroEntity hero) {
        if (hero == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El héroe no puede ser nulo"))
                    .build();
        }
        
        // Verificar si ya existe
        if (hero.name != null && heroRepository.findByNameIgnoreCase(hero.name).isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Ya existe un héroe con el nombre: " + hero.name))
                    .build();
        }
        
        heroRepository.persist(hero);
        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                    "hero", hero,
                    "pattern", "Repository Pattern",
                    "note", "Héroe creado usando heroRepository.persist()"
                ))
                .build();
    }
    
    /**
     * Actualizar héroe usando Repository Pattern
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateHero(@PathParam("id") Long id, HeroEntity heroUpdate) {
        HeroEntity hero = heroRepository.findById(id);
        
        if (hero == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Héroe no encontrado con ID: " + id))
                    .build();
        }
        
        // Actualizar campos
        if (heroUpdate.name != null) hero.name = heroUpdate.name;
        if (heroUpdate.power != null) hero.power = heroUpdate.power;
        if (heroUpdate.powerLevel != null) hero.powerLevel = heroUpdate.powerLevel;
        if (heroUpdate.description != null) hero.description = heroUpdate.description;
        if (heroUpdate.isActive != null) hero.isActive = heroUpdate.isActive;
        
        heroRepository.persist(hero);
        
        return Response.ok(Map.of(
            "hero", hero,
            "pattern", "Repository Pattern",
            "note", "Héroe actualizado usando heroRepository.persist()"
        )).build();
    }
    
    /**
     * Eliminar héroe usando Repository Pattern
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteHero(@PathParam("id") Long id) {
        HeroEntity hero = heroRepository.findById(id);
        
        if (hero == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Héroe no encontrado con ID: " + id))
                    .build();
        }
        
        heroRepository.delete(hero);
        
        return Response.ok(Map.of(
            "message", "Héroe eliminado exitosamente",
            "id", id,
            "pattern", "Repository Pattern",
            "note", "Héroe eliminado usando heroRepository.delete()"
        )).build();
    }
    
    /**
     * Estadísticas usando Repository Pattern
     */
    @GET
    @Path("/stats")
    public Response getStats() {
        long totalHeroes = heroRepository.count();
        long powerfulHeroes = heroRepository.countPowerfulHeroes();
        long activeHeroes = heroRepository.count("isActive", true);
        
        return Response.ok(Map.of(
            "totalHeroes", totalHeroes,
            "powerfulHeroes", powerfulHeroes,
            "activeHeroes", activeHeroes,
            "pattern", "Repository Pattern",
            "note", "Estadísticas obtenidas usando métodos del repositorio"
        )).build();
    }
}

