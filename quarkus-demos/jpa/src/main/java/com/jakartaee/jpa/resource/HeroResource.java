package com.jakartaee.jpa.resource;

import com.jakartaee.jpa.entity.HeroJPA;
import com.jakartaee.jpa.service.HeroService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Recurso REST para Heroes que demuestra el uso de JPA.
 * Muestra diferentes operaciones con EntityManager, Named Queries, JPQL y Criteria API.
 */
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeroResource {
    
    @Inject
    HeroService heroService;
    
    /**
     * Obtiene todos los héroes usando Named Query.
     * GET /api/heroes
     */
    @GET
    public Response getAllHeroes() {
        List<HeroJPA> heroes = heroService.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene un héroe por ID usando EntityManager.find().
     * GET /api/heroes/{id}
     */
    @GET
    @Path("/{id}")
    public Response getHero(@PathParam("id") Long id) {
        Optional<HeroJPA> hero = heroService.getHeroById(id);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    /**
     * Busca héroes por nombre usando Named Query.
     * GET /api/heroes/search?name=Superman
     */
    @GET
    @Path("/search")
    public Response searchHeroByName(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter is required")
                    .build();
        }
        
        Optional<HeroJPA> hero = heroService.findHeroByName(name);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with name: " + name)
                        .build());
    }
    
    /**
     * Busca héroes poderosos usando Named Query.
     * GET /api/heroes/powerful?minLevel=80
     */
    @GET
    @Path("/powerful")
    public Response getPowerfulHeroes(@QueryParam("minLevel") @DefaultValue("80") int minLevel) {
        List<HeroJPA> heroes = heroService.findPowerfulHeroes(minLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes por rango de poder usando Named Query.
     * GET /api/heroes/power-range?minLevel=80&maxLevel=100
     */
    @GET
    @Path("/power-range")
    public Response getHeroesByPowerRange(
            @QueryParam("minLevel") @DefaultValue("80") int minLevel,
            @QueryParam("maxLevel") @DefaultValue("100") int maxLevel) {
        List<HeroJPA> heroes = heroService.findHeroesByPowerRange(minLevel, maxLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes usando Criteria API.
     * GET /api/heroes/criteria?minPowerLevel=80&activeOnly=true
     */
    @GET
    @Path("/criteria")
    public Response findHeroesWithCriteria(
            @QueryParam("minPowerLevel") @DefaultValue("80") int minPowerLevel,
            @QueryParam("activeOnly") @DefaultValue("false") boolean activeOnly) {
        List<HeroJPA> heroes = heroService.findHeroesWithCriteria(minPowerLevel, activeOnly);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes usando Criteria API con múltiples condiciones.
     * GET /api/heroes/advanced-search?namePattern=Super&minPowerLevel=80&maxPowerLevel=100
     */
    @GET
    @Path("/advanced-search")
    public Response findHeroesAdvanced(
            @QueryParam("namePattern") String namePattern,
            @QueryParam("minPowerLevel") @DefaultValue("0") int minPowerLevel,
            @QueryParam("maxPowerLevel") @DefaultValue("100") int maxPowerLevel) {
        List<HeroJPA> heroes = heroService.findHeroesWithMultipleCriteria(
                namePattern, minPowerLevel, maxPowerLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene héroes con sus misiones usando JOIN FETCH.
     * GET /api/heroes/with-missions
     */
    @GET
    @Path("/with-missions")
    public Response getHeroesWithMissions() {
        List<HeroJPA> heroes = heroService.findHeroesWithMissions();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene estadísticas de héroes usando JPQL con funciones de agregación.
     * GET /api/heroes/statistics
     */
    @GET
    @Path("/statistics")
    public Response getHeroStatistics() {
        Map<String, Object> stats = heroService.getHeroStatistics();
        return Response.ok(stats).build();
    }
    
    /**
     * Crea un nuevo héroe usando EntityManager.persist().
     * POST /api/heroes
     */
    @POST
    public Response createHero(HeroJPA hero) {
        HeroJPA created = heroService.createHero(hero);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    /**
     * Actualiza un héroe usando EntityManager.merge().
     * PUT /api/heroes/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateHero(@PathParam("id") Long id, HeroJPA heroUpdate) {
        Optional<HeroJPA> heroOpt = heroService.getHeroById(id);
        if (heroOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        
        HeroJPA hero = heroOpt.get();
        hero.setName(heroUpdate.getName());
        hero.setPower(heroUpdate.getPower());
        hero.setPowerLevel(heroUpdate.getPowerLevel());
        hero.setDescription(heroUpdate.getDescription());
        hero.setIsActive(heroUpdate.getIsActive());
        
        HeroJPA updated = heroService.updateHero(hero);
        return Response.ok(updated).build();
    }
    
    /**
     * Actualiza el nivel de poder usando Named Query UPDATE.
     * PATCH /api/heroes/{id}/power-level?newLevel=95
     */
    @PATCH
    @Path("/{id}/power-level")
    public Response updatePowerLevel(
            @PathParam("id") Long id,
            @QueryParam("newLevel") int newLevel) {
        int updated = heroService.updatePowerLevel(id, newLevel);
        if (updated > 0) {
            return Response.ok(Map.of("updated", updated)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
    }
    
    /**
     * Elimina un héroe usando EntityManager.remove().
     * DELETE /api/heroes/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        heroService.deleteHero(id);
        return Response.noContent().build();
    }
}

