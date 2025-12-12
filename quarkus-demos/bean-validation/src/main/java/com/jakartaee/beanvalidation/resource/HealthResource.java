package com.jakartaee.beanvalidation.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Recurso de prueba para verificar que los recursos REST funcionan.
 */
@Path("/health")
public class HealthResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok(Map.of("status", "ok", "message", "Bean Validation Demo is running")).build();
    }
}

