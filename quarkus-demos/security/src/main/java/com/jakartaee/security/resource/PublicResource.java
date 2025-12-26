package com.jakartaee.security.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * Recurso público que no requiere autenticación.
 * Demuestra endpoints accesibles sin credenciales.
 */
@Path("/api/public")
@Produces(MediaType.APPLICATION_JSON)
public class PublicResource {
    
    @GET
    @Path("/info")
    public Response getPublicInfo() {
        return Response.ok(Map.of(
                "message", "Este es un endpoint público",
                "description", "No se requiere autenticación para acceder a este recurso",
                "endpoints", Map.of(
                        "public", "/api/public/*",
                        "protected", "/api/protected/*",
                        "admin", "/api/admin/*"
                )
        )).build();
    }
    
    @GET
    @Path("/heroes")
    public Response getPublicHeroes() {
        return Response.ok(Map.of(
                "heroes", "Información pública sobre héroes",
                "note", "Para información detallada, autentícate como HERO o ADMIN"
        )).build();
    }
}

