package com.jakartaee.security.resource;

import com.jakartaee.common.entities.Hero;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Recurso protegido que requiere autenticación.
 * Demuestra autorización basada en roles.
 */
@Path("/api/protected")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated // Requiere autenticación para todos los endpoints
public class ProtectedResource {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    SecurityContext securityContext;
    
    /**
     * Endpoint accesible para cualquier usuario autenticado.
     */
    @GET
    @Path("/profile")
    public Response getProfile() {
        Principal principal = securityContext.getUserPrincipal();
        String username = principal != null ? principal.getName() : "anonymous";
        
        return Response.ok(Map.of(
                "username", username,
                "authenticated", true,
                "roles", getRoles(),
                "message", "Información de perfil del usuario autenticado"
        )).build();
    }
    
    /**
     * Endpoint accesible solo para usuarios con rol HERO.
     */
    @GET
    @Path("/heroes")
    @RolesAllowed("HERO")
    public Response getHeroes() {
        List<Hero> heroes = entityManager.createQuery(
                "SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
        
        return Response.ok(Map.of(
                "heroes", heroes,
                "message", "Solo usuarios con rol HERO pueden ver esta información",
                "user", securityContext.getUserPrincipal().getName()
        )).build();
    }
    
    /**
     * Endpoint accesible solo para usuarios con rol VILLAIN.
     */
    @GET
    @Path("/villains")
    @RolesAllowed("VILLAIN")
    public Response getVillains() {
        return Response.ok(Map.of(
                "message", "Solo usuarios con rol VILLAIN pueden ver esta información",
                "user", securityContext.getUserPrincipal().getName(),
                "note", "Los villanos tienen acceso a información privilegiada"
        )).build();
    }
    
    /**
     * Endpoint accesible para usuarios con rol HERO o ADMIN.
     */
    @GET
    @Path("/heroes/{id}")
    @RolesAllowed({"HERO", "ADMIN"})
    public Response getHeroById(@PathParam("id") Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            return Response.ok(Map.of(
                    "hero", hero,
                    "user", securityContext.getUserPrincipal().getName(),
                    "role", getRoles()
            )).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Hero not found"))
                .build();
    }
    
    /**
     * Endpoint accesible solo para ADMIN.
     */
    @POST
    @Path("/heroes")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response createHero(@QueryParam("name") String name,
                               @QueryParam("power") String power,
                               @QueryParam("powerLevel") Integer powerLevel) {
        Hero hero = new Hero(name, power, powerLevel);
        entityManager.persist(hero);
        entityManager.flush();
        
        return Response.status(Response.Status.CREATED).entity(Map.of(
                "hero", hero,
                "message", "Hero creado por administrador",
                "user", securityContext.getUserPrincipal().getName()
        )).build();
    }
    
    /**
     * Endpoint accesible solo para ADMIN.
     */
    @DELETE
    @Path("/heroes/{id}")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response deleteHero(@PathParam("id") Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            entityManager.remove(hero);
            return Response.ok(Map.of(
                    "message", "Hero eliminado",
                    "id", id,
                    "user", securityContext.getUserPrincipal().getName()
            )).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Hero not found"))
                .build();
    }
    
    /**
     * Obtener roles del usuario actual.
     */
    private List<String> getRoles() {
        // En Quarkus Security, los roles están disponibles a través de SecurityContext
        // Para simplificar, retornamos una lista basada en el usuario
        String username = securityContext.getUserPrincipal().getName();
        return switch (username) {
            case "superman" -> List.of("HERO", "ADMIN");
            case "batman" -> List.of("HERO");
            case "joker", "lex" -> List.of("VILLAIN");
            default -> List.of();
        };
    }
}

