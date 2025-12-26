package com.jakartaee.interceptors.resource;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.interceptors.service.HeroService;
import com.jakartaee.interceptors.service.PowerAnalysisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

/**
 * Recurso REST que demuestra el uso de interceptores.
 * Cada endpoint muestra diferentes combinaciones de interceptores.
 */
@Path("/api/interceptors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InterceptorDemoResource {
    
    @Inject
    HeroService heroService;
    
    @Inject
    PowerAnalysisService powerAnalysisService;
    
    /**
     * Endpoint que demuestra: Logging + Timing + Validación + Caché
     * La primera llamada ejecutará el método, las siguientes usarán caché.
     */
    @GET
    @Path("/heroes")
    public Response getAllHeroes() {
        List<Hero> heroes = heroService.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Endpoint que demuestra: Logging + Timing + Validación
     */
    @GET
    @Path("/heroes/{id}")
    public Response getHeroById(@PathParam("id") Long id) {
        return heroService.getHeroById(id)
                .map(hero -> Response.ok(hero).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    /**
     * Endpoint que demuestra: Logging + Timing + Validación
     * Si se envía un parámetro null, el ValidationInterceptor lanzará una excepción.
     */
    @POST
    @Path("/heroes")
    public Response createHero(@QueryParam("name") String name,
                               @QueryParam("power") String power,
                               @QueryParam("powerLevel") Integer powerLevel) {
        try {
            Hero hero = heroService.createHero(name, power, powerLevel);
            return Response.status(Response.Status.CREATED).entity(hero).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Validation error: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Endpoint que demuestra: Logging + Timing
     */
    @GET
    @Path("/heroes/powerful")
    public Response getPowerfulHeroes(@QueryParam("minPowerLevel") @DefaultValue("80") int minPowerLevel) {
        List<Hero> heroes = heroService.findHeroesByPowerLevel(minPowerLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Endpoint que demuestra interceptores encadenados y caché.
     * La primera llamada ejecutará el análisis, las siguientes usarán caché.
     */
    @GET
    @Path("/analysis")
    public Response analyzePowers() {
        Map<String, Object> analysis = powerAnalysisService.analyzePowers();
        return Response.ok(analysis).build();
    }
    
    /**
     * Endpoint que demuestra: Logging + Timing
     */
    @GET
    @Path("/powers")
    public Response getPowerTypes() {
        List<String> powerTypes = powerAnalysisService.getPowerTypes();
        return Response.ok(powerTypes).build();
    }
    
    /**
     * Endpoint de información sobre los interceptores disponibles.
     */
    @GET
    @Path("/info")
    public Response getInterceptorInfo() {
        Map<String, Object> info = Map.of(
                "interceptors", List.of(
                        Map.of("name", "@Loggable", "type", "@AroundInvoke", 
                                "description", "Registra llamadas a métodos con parámetros y retornos"),
                        Map.of("name", "@Timed", "type", "@AroundInvoke", 
                                "description", "Mide el tiempo de ejecución de métodos"),
                        Map.of("name", "@Validated", "type", "@AroundInvoke", 
                                "description", "Valida parámetros antes de ejecutar métodos"),
                        Map.of("name", "@Cached", "type", "@AroundInvoke", 
                                "description", "Cachea resultados de métodos"),
                        Map.of("name", "@Tracked", "type", "@AroundConstruct", 
                                "description", "Rastrea la creación de objetos"),
                        Map.of("name", "@Monitored", "type", "@PostConstruct/@PreDestroy", 
                                "description", "Monitorea el ciclo de vida de beans")
                ),
                "usage", "Los interceptores se aplican automáticamente a métodos/clases marcados con las anotaciones correspondientes"
        );
        return Response.ok(info).build();
    }
}

