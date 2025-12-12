package com.jakartaee.jakartadata.resource;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.jakartadata.service.HeroService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Recurso REST para gestionar Heroes usando Jakarta Data.
 * Demuestra cómo usar repositorios Jakarta Data desde endpoints REST.
 */
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeroResource {
    
    @Inject
    HeroService heroService;
    
    /**
     * Lista todos los héroes.
     * GET /api/heroes
     */
    @GET
    public Response getAllHeroes() {
        List<Hero> heroes = heroService.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene un héroe por ID.
     * GET /api/heroes/{id}
     */
    @GET
    @Path("/{id}")
    public Response getHero(@PathParam("id") Long id) {
        Optional<Hero> hero = heroService.getHeroById(id);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    /**
     * Busca héroes por nombre (usa patrón como en la demo).
     * GET /api/heroes/search?name=Superman
     */
    @GET
    @Path("/search")
    public Response searchHeroesByName(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter is required")
                    .build();
        }
        // Convertir a patrón para búsqueda parcial
        String pattern = "%" + name.replace('*', '%') + "%";
        List<Hero> heroes = heroService.findHeroesByName(pattern);
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene todos los héroes activos.
     * GET /api/heroes/active
     */
    @GET
    @Path("/active")
    public Response getActiveHeroes() {
        List<Hero> heroes = heroService.getActiveHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Crea un nuevo héroe.
     * POST /api/heroes
     */
    @POST
    public Response createHero(Hero hero) {
        heroService.createHero(hero);
        return Response.status(Response.Status.CREATED).entity(hero).build();
    }
    
    /**
     * Actualiza un héroe existente.
     * PUT /api/heroes/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateHero(@PathParam("id") Long id, Hero hero) {
        Optional<Hero> existingHero = heroService.getHeroById(id);
        if (existingHero.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        hero.setId(id); // Asegurar que el ID coincida
        heroService.updateHero(hero);
        return Response.ok(hero).build();
    }
    
    /**
     * Elimina un héroe.
     * DELETE /api/heroes/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        Optional<Hero> hero = heroService.getHeroById(id);
        if (hero.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        heroService.deleteHero(id);
        return Response.noContent().build();
    }
}
