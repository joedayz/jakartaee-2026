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
 * Recurso de administración que requiere rol ADMIN.
 * Demuestra endpoints exclusivos para administradores.
 */
@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
@RolesAllowed("ADMIN") // Todos los endpoints requieren rol ADMIN
public class AdminResource {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    SecurityContext securityContext;
    
    @GET
    @Path("/dashboard")
    public Response getDashboard() {
        Principal principal = securityContext.getUserPrincipal();
        String username = principal != null ? principal.getName() : "anonymous";
        
        long heroCount = entityManager.createQuery(
                "SELECT COUNT(h) FROM Hero h", Long.class)
                .getSingleResult();
        
        return Response.ok(Map.of(
                "user", username,
                "role", "ADMIN",
                "heroCount", heroCount,
                "message", "Panel de administración",
                "capabilities", List.of(
                        "Ver todos los héroes",
                        "Crear nuevos héroes",
                        "Eliminar héroes",
                        "Gestionar usuarios"
                )
        )).build();
    }
    
    @GET
    @Path("/heroes")
    public Response getAllHeroes() {
        List<Hero> heroes = entityManager.createQuery(
                "SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
        
        return Response.ok(Map.of(
                "heroes", heroes,
                "total", heroes.size(),
                "user", securityContext.getUserPrincipal().getName()
        )).build();
    }
    
    @PUT
    @Path("/heroes/{id}")
    @Transactional
    public Response updateHero(@PathParam("id") Long id,
                               @QueryParam("name") String name,
                               @QueryParam("power") String power,
                               @QueryParam("powerLevel") Integer powerLevel) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero != null) {
            if (name != null) hero.setName(name);
            if (power != null) hero.setPower(power);
            if (powerLevel != null) hero.setPowerLevel(powerLevel);
            entityManager.merge(hero);
            
            return Response.ok(Map.of(
                    "hero", hero,
                    "message", "Hero actualizado por administrador",
                    "user", securityContext.getUserPrincipal().getName()
            )).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Hero not found"))
                .build();
    }
    
    @GET
    @Path("/users")
    public Response getUsers() {
        return Response.ok(Map.of(
                "message", "Lista de usuarios (solo ADMIN)",
                "users", List.of(
                        Map.of("username", "superman", "roles", List.of("HERO", "ADMIN")),
                        Map.of("username", "batman", "roles", List.of("HERO")),
                        Map.of("username", "joker", "roles", List.of("VILLAIN")),
                        Map.of("username", "lex", "roles", List.of("VILLAIN", "ADMIN"))
                ),
                "user", securityContext.getUserPrincipal().getName()
        )).build();
    }
}

