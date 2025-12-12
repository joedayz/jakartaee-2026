package com.jakartaee.beanvalidation.resource;

import com.jakartaee.beanvalidation.dto.HeroCreateDTO;
import com.jakartaee.beanvalidation.dto.HeroUpdateDTO;
import com.jakartaee.beanvalidation.service.HeroService;
import com.jakartaee.common.entities.Hero;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Recurso REST para gestionar Heroes con Bean Validation.
 * Demuestra validaciones en diferentes niveles:
 * - Validaciones automáticas en parámetros de método (@PathParam, @QueryParam)
 * - Validaciones en DTOs usando @Valid
 * - Validaciones personalizadas en DTOs
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
        if (id == null || id < 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID debe ser mayor que 0")
                    .build();
        }
        Optional<Hero> hero = heroService.getHeroById(id);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    /**
     * Busca héroes por nombre.
     * GET /api/heroes/search?name=Superman
     */
    @GET
    @Path("/search")
    public Response searchHeroesByName(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro 'name' es requerido")
                    .build();
        }
        List<Hero> heroes = heroService.findHeroesByName(name);
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
     * Crea un nuevo héroe con validación automática.
     * POST /api/heroes
     * Demuestra:
     * - @Valid para validar el DTO automáticamente
     * - Validaciones personalizadas en el DTO
     */
    @POST
    public Response createHero(
            @Valid 
            HeroCreateDTO dto) {
        Hero hero = heroService.createHero(dto);
        return Response.status(Response.Status.CREATED).entity(hero).build();
    }
    
    /**
     * Actualiza un héroe existente con validación automática.
     * PUT /api/heroes/{id}
     * Demuestra @Valid para validar el DTO automáticamente.
     */
    @PUT
    @Path("/{id}")
    public Response updateHero(@PathParam("id") Long id, @Valid HeroUpdateDTO dto) {
        if (id == null || id < 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID debe ser mayor que 0")
                    .build();
        }
        // Asegurar que el ID del path coincida con el del DTO
        if (!id.equals(dto.id())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID del path no coincide con el ID del DTO")
                    .build();
        }
        
        Hero hero = heroService.updateHero(dto);
        return Response.ok(hero).build();
    }
    
    /**
     * Elimina un héroe.
     * DELETE /api/heroes/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        if (id == null || id < 1) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El ID debe ser mayor que 0")
                    .build();
        }
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

