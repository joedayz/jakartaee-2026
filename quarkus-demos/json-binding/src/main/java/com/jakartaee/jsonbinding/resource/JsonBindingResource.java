package com.jakartaee.jsonbinding.resource;

import com.jakartaee.jsonbinding.model.*;
import com.jakartaee.jsonbinding.service.JsonBindingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso REST que demuestra características de JSON-Binding.
 * Muestra serialización/deserialización automática y manual.
 */
@Path("/api/json-binding")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonBindingResource {
    
    @Inject
    JsonBindingService jsonBindingService;
    
    /**
     * Obtiene un team de ejemplo.
     * Demuestra serialización automática por Quarkus REST.
     * GET /api/json-binding/team/example
     */
    @GET
    @Path("/team/example")
    public Response getExampleTeam() {
        Team team = jsonBindingService.createExampleTeam();
        return Response.ok(team).build();
    }
    
    /**
     * Obtiene múltiples teams de ejemplo.
     * GET /api/json-binding/teams/example
     */
    @GET
    @Path("/teams/example")
    public Response getExampleTeams() {
        List<Team> teams = jsonBindingService.createExampleTeams();
        return Response.ok(teams).build();
    }
    
    /**
     * Crea un team desde JSON.
     * Demuestra deserialización automática por Quarkus REST.
     * POST /api/json-binding/team
     */
    @POST
    @Path("/team")
    public Response createTeam(Team team) {
        // El team ya está deserializado automáticamente
        String json = jsonBindingService.serializeTeam(team);
        return Response.status(Response.Status.CREATED)
                .entity(team)
                .build();
    }
    
    /**
     * Serializa un team manualmente a JSON string.
     * GET /api/json-binding/team/{id}/serialize
     */
    @GET
    @Path("/team/{id}/serialize")
    @Produces(MediaType.TEXT_PLAIN)
    public Response serializeTeam(@PathParam("id") Long id) {
        Team team = jsonBindingService.createExampleTeam();
        team.setId(id);
        String json = jsonBindingService.serializeTeam(team);
        return Response.ok(json).build();
    }
    
    /**
     * Deserializa JSON string a Team manualmente.
     * POST /api/json-binding/team/deserialize
     */
    @POST
    @Path("/team/deserialize")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deserializeTeam(String json) {
        Team team = jsonBindingService.deserializeTeam(json);
        return Response.ok(team).build();
    }
    
    /**
     * Serializa con configuración personalizada.
     * GET /api/json-binding/team/{id}/custom-config
     */
    @GET
    @Path("/team/{id}/custom-config")
    @Produces(MediaType.TEXT_PLAIN)
    public Response serializeWithCustomConfig(@PathParam("id") Long id) {
        Team team = jsonBindingService.createExampleTeam();
        team.setId(id);
        String json = jsonBindingService.serializeWithCustomConfig(team);
        return Response.ok(json).build();
    }
    
    /**
     * Obtiene un héroe con custom adapter.
     * Demuestra cómo un campo se serializa usando un adapter personalizado.
     * GET /api/json-binding/hero/adapter-example
     */
    @GET
    @Path("/hero/adapter-example")
    public Response getHeroWithAdapter() {
        HeroWithCustomAdapter hero = jsonBindingService.createHeroWithAdapter();
        return Response.ok(hero).build();
    }
    
    /**
     * Serializa un héroe con custom adapter manualmente.
     * GET /api/json-binding/hero/adapter-example/serialize
     */
    @GET
    @Path("/hero/adapter-example/serialize")
    @Produces(MediaType.TEXT_PLAIN)
    public Response serializeHeroWithAdapter() {
        HeroWithCustomAdapter hero = jsonBindingService.createHeroWithAdapter();
        String json = jsonBindingService.serializeHeroWithAdapter(hero);
        return Response.ok(json).build();
    }
    
    /**
     * Información sobre características de JSON-Binding demostradas.
     * GET /api/json-binding/info
     */
    @GET
    @Path("/info")
    public Response getInfo() {
        return Response.ok(java.util.Map.of(
            "features", java.util.List.of(
                "Automatic serialization/deserialization in REST endpoints",
                "@JsonbProperty - Rename fields",
                "@JsonbTransient - Exclude fields",
                "@JsonbDateFormat - Format dates",
                "@JsonbPropertyOrder - Order fields",
                "@JsonbTypeAdapter - Custom adapters",
                "JsonbConfig - Custom configuration",
                "Nested objects",
                "Collections (List, Set, Map)",
                "Manual serialization/deserialization"
            ),
            "annotations", java.util.Map.of(
                "@JsonbProperty", "Rename JSON field",
                "@JsonbTransient", "Exclude from JSON",
                "@JsonbDateFormat", "Format date fields",
                "@JsonbPropertyOrder", "Order fields in JSON",
                "@JsonbTypeAdapter", "Use custom adapter"
            )
        )).build();
    }
}

