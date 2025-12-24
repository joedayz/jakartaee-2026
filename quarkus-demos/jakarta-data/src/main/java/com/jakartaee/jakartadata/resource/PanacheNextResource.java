package com.jakartaee.jakartadata.resource;

import com.jakartaee.jakartadata.entity.HeroPanacheEntity;
import com.jakartaee.jakartadata.entity.VillainPanacheEntity;
import com.jakartaee.jakartadata.service.PanacheNextService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Recurso REST que demuestra Panache Next (Hibernate with Panache).
 * Muestra el uso de:
 * - Active Record Pattern
 * - Repositorios anidados con @HQL
 * - Queries type-safe
 */
@Path("/api/panache-next")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PanacheNextResource {
    
    @Inject
    PanacheNextService panacheNextService;
    
    /**
     * Crea un héroe usando Active Record Pattern.
     * POST /api/panache-next/heroes/active-record
     */
    @POST
    @Path("/heroes/active-record")
    public Response createHeroWithActiveRecord(HeroPanacheEntity hero) {
        HeroPanacheEntity created = panacheNextService.createHeroWithActiveRecord(
                hero.name, hero.power, hero.powerLevel);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    /**
     * Crea un héroe usando el repositorio anidado.
     * POST /api/panache-next/heroes/repository
     */
    @POST
    @Path("/heroes/repository")
    public Response createHeroWithRepository(HeroPanacheEntity hero) {
        HeroPanacheEntity created = panacheNextService.createHeroWithRepository(
                hero.name, hero.power, hero.powerLevel);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    /**
     * Obtiene héroes poderosos usando @HQL.
     * GET /api/panache-next/heroes/powerful?minLevel=80
     */
    @GET
    @Path("/heroes/powerful")
    public Response getPowerfulHeroes(@QueryParam("minLevel") @DefaultValue("80") int minLevel) {
        List<HeroPanacheEntity> heroes = panacheNextService.findPowerfulHeroes(minLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene héroes activos usando @HQL.
     * GET /api/panache-next/heroes/active
     */
    @GET
    @Path("/heroes/active")
    public Response getActiveHeroes() {
        List<HeroPanacheEntity> heroes = panacheNextService.findActiveHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene héroes por rango de poder usando @HQL.
     * GET /api/panache-next/heroes/power-range?minLevel=80&maxLevel=100
     */
    @GET
    @Path("/heroes/power-range")
    public Response getHeroesByPowerRange(
            @QueryParam("minLevel") @DefaultValue("80") int minLevel,
            @QueryParam("maxLevel") @DefaultValue("100") int maxLevel) {
        List<HeroPanacheEntity> heroes = panacheNextService.findHeroesByPowerRange(minLevel, maxLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene un héroe por ID.
     * GET /api/panache-next/heroes/{id}
     */
    @GET
    @Path("/heroes/{id}")
    public Response getHero(@PathParam("id") Long id) {
        Optional<HeroPanacheEntity> hero = panacheNextService.getHeroById(id);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    /**
     * Actualiza un héroe usando Active Record Pattern.
     * PUT /api/panache-next/heroes/{id}
     */
    @PUT
    @Path("/heroes/{id}")
    public Response updateHero(@PathParam("id") Long id, HeroPanacheEntity heroUpdate) {
        try {
            HeroPanacheEntity updated = panacheNextService.updateHeroWithActiveRecord(
                    id, heroUpdate.name);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    /**
     * Elimina un héroe usando Active Record Pattern.
     * DELETE /api/panache-next/heroes/{id}
     */
    @DELETE
    @Path("/heroes/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        panacheNextService.deleteHeroWithActiveRecord(id);
        return Response.noContent().build();
    }
    
    /**
     * Elimina un héroe por nombre usando @HQL.
     * DELETE /api/panache-next/heroes/by-name/{name}
     */
    @DELETE
    @Path("/heroes/by-name/{name}")
    public Response deleteHeroByName(@PathParam("name") String name) {
        long deleted = panacheNextService.deleteHeroByName(name);
        return Response.ok(Map.of("deleted", deleted)).build();
    }
    
    /**
     * Cuenta héroes activos usando @HQL.
     * GET /api/panache-next/heroes/count/active
     */
    @GET
    @Path("/heroes/count/active")
    public Response countActiveHeroes() {
        long count = panacheNextService.countActiveHeroes();
        return Response.ok(Map.of("count", count)).build();
    }
    
    /**
     * Obtiene todos los héroes ordenados por poder usando @HQL.
     * GET /api/panache-next/heroes/ordered-by-power
     */
    @GET
    @Path("/heroes/ordered-by-power")
    public Response getAllHeroesOrderedByPower() {
        List<HeroPanacheEntity> heroes = panacheNextService.getAllHeroesOrderedByPower();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene villanos peligrosos usando @HQL.
     * GET /api/panache-next/villains/dangerous
     */
    @GET
    @Path("/villains/dangerous")
    public Response getDangerousVillains() {
        List<VillainPanacheEntity> villains = panacheNextService.findDangerousVillains();
        return Response.ok(villains).build();
    }
    
    /**
     * Obtiene villanos por nivel de amenaza usando @HQL.
     * GET /api/panache-next/villains/threat-level/{level}
     */
    @GET
    @Path("/villains/threat-level/{level}")
    public Response getVillainsByThreatLevel(@PathParam("level") String threatLevel) {
        List<VillainPanacheEntity> villains = panacheNextService.findVillainsByThreatLevel(threatLevel);
        return Response.ok(villains).build();
    }
}

