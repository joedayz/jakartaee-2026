package com.jakartaee.cdi.resource;

import com.jakartaee.cdi.qualifier.HeroQualifier;
import com.jakartaee.cdi.service.HeroService;
import com.jakartaee.common.entities.Hero;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Recurso REST para gestionar Heroes.
 * Demuestra inyección de dependencias con qualifiers.
 */
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HeroResource {
    
    @Inject
    @HeroQualifier
    HeroService heroService;
    
    @GET
    public Response getAllHeroes() {
        List<Hero> heroes = heroService.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getHero(@PathParam("id") Long id) {
        Optional<Hero> hero = heroService.getHeroById(id);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    @GET
    @Path("/search")
    public Response searchHeroes(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter is required")
                    .build();
        }
        List<Hero> heroes = heroService.findHeroesByName(name);
        return Response.ok(heroes).build();
    }
    
    @GET
    @Path("/powerful")
    public Response getPowerfulHeroes() {
        List<Hero> heroes = heroService.getPowerfulHeroes();
        return Response.ok(heroes).build();
    }
    
    @POST
    public Response createHero(Hero hero) {
        Hero created = heroService.createHero(hero);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateHero(@PathParam("id") Long id, Hero hero) {
        Optional<Hero> existing = heroService.getHeroById(id);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Hero not found with id: " + id)
                    .build();
        }
        hero.setId(id);
        Hero updated = heroService.updateHero(hero);
        return Response.ok(updated).build();
    }
    
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

