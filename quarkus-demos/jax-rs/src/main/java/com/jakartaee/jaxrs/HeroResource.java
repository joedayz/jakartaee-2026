package com.jakartaee.jaxrs;

import com.jakartaee.common.dto.HeroDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Recurso REST para gestionar Heroes de DC Comics.
 * Demuestra el uso de Jakarta RESTful Web Services (JAX-RS).
 */
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeroResource {
    
    // Almacenamiento en memoria (en producción usaría JPA)
    private final ConcurrentHashMap<Long, HeroDTO> heroes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public HeroResource() {
        // Datos de ejemplo
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        createHero(new HeroDTO(null, "Superman", "Super fuerza, vuelo, visión de rayos X", 95));
        createHero(new HeroDTO(null, "Batman", "Inteligencia, artes marciales, tecnología", 85));
        createHero(new HeroDTO(null, "Wonder Woman", "Super fuerza, vuelo, lazo de la verdad", 90));
        createHero(new HeroDTO(null, "Flash", "Super velocidad", 88));
        createHero(new HeroDTO(null, "Green Lantern", "Anillo de poder", 87));
    }
    
    /**
     * Lista todos los héroes.
     * GET /api/heroes
     */
    @GET
    public Response getAllHeroes() {
        List<HeroDTO> heroList = new ArrayList<>(heroes.values());
        return Response.ok(heroList).build();
    }
    
    /**
     * Obtiene un héroe por ID.
     * GET /api/heroes/{id}
     */
    @GET
    @Path("/{id}")
    public Response getHero(@PathParam("id") Long id) {
        HeroDTO hero = heroes.get(id);
        if (hero == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        return Response.ok(hero).build();
    }
    
    /**
     * Busca héroes por nombre.
     * GET /api/heroes/search?name=Superman
     */
    @GET
    @Path("/search")
    public Response searchHeroes(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Query parameter 'name' is required")
                    .build();
        }
        
        List<HeroDTO> results = heroes.values().stream()
                .filter(hero -> hero.name().toLowerCase().contains(name.toLowerCase()))
                .toList();
        
        return Response.ok(results).build();
    }
    
    /**
     * Crea un nuevo héroe.
     * POST /api/heroes
     */
    @POST
    public Response createHero(HeroDTO hero) {
        if (hero.name() == null || hero.name().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Hero name is required")
                    .build();
        }
        
        Long id = idGenerator.getAndIncrement();
        // Crear nuevo HeroDTO con el ID asignado (Records son inmutables)
        HeroDTO heroWithId = new HeroDTO(
            id,
            hero.name(),
            hero.power(),
            hero.powerLevel(),
            hero.description(),
            hero.isActive()
        );
        heroes.put(id, heroWithId);
        
        return Response.status(Response.Status.CREATED)
                .entity(heroWithId)
                .build();
    }
    
    /**
     * Actualiza un héroe existente.
     * PUT /api/heroes/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateHero(@PathParam("id") Long id, HeroDTO hero) {
        HeroDTO existing = heroes.get(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        
        // Crear nuevo HeroDTO con el ID (Records son inmutables)
        HeroDTO updatedHero = new HeroDTO(
            id,
            hero.name(),
            hero.power(),
            hero.powerLevel(),
            hero.description(),
            hero.isActive()
        );
        heroes.put(id, updatedHero);
        
        return Response.ok(hero).build();
    }
    
    /**
     * Elimina un héroe.
     * DELETE /api/heroes/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        HeroDTO removed = heroes.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        
        return Response.noContent().build();
    }
    
    /**
     * Obtiene estadísticas de los héroes.
     * GET /api/heroes/stats
     */
    @GET
    @Path("/stats")
    public Response getStats() {
        if (heroes.isEmpty()) {
            return Response.ok("No heroes available").build();
        }
        
        double avgPowerLevel = heroes.values().stream()
                .mapToInt(HeroDTO::powerLevel)
                .average()
                .orElse(0.0);
        
        int maxPowerLevel = heroes.values().stream()
                .mapToInt(HeroDTO::powerLevel)
                .max()
                .orElse(0);
        
        String stats = String.format(
                "Total Heroes: %d, Average Power Level: %.2f, Max Power Level: %d",
                heroes.size(), avgPowerLevel, maxPowerLevel
        );
        
        return Response.ok(stats).build();
    }
}

