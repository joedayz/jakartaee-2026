package com.jakartaee.cdi.resource;

import com.jakartaee.cdi.observer.EventObserver;
import com.jakartaee.cdi.producer.ConfigurationProducer;
import com.jakartaee.cdi.service.PowerAnalysisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

/**
 * Recurso REST que demuestra diferentes características de CDI:
 * - Inyección de dependencias
 * - Producers
 * - Observers
 * - Scopes
 */
@Path("/api/cdi-demo")
@Produces(MediaType.APPLICATION_JSON)
public class CDIDemoResource {
    
    @Inject
    PowerAnalysisService powerAnalysisService;
    
    @Inject
    EventObserver eventObserver;
    
    @Inject
    ConfigurationProducer.ApplicationConfig applicationConfig;
    
    /**
     * Demuestra el uso de servicios inyectados con qualifiers.
     */
    @GET
    @Path("/power-analysis")
    public Response getPowerAnalysis() {
        Map<String, Object> analysis = powerAnalysisService.analyzePowerLevels();
        return Response.ok(analysis).build();
    }
    
    /**
     * Demuestra el uso de observers y eventos.
     */
    @GET
    @Path("/events")
    public Response getEvents() {
        Map<String, Object> events = Map.of(
            "heroEvents", eventObserver.getHeroEventCount(),
            "villainEvents", eventObserver.getVillainEventCount(),
            "totalEvents", eventObserver.getHeroEventCount() + eventObserver.getVillainEventCount()
        );
        return Response.ok(events).build();
    }
    
    /**
     * Demuestra el uso de producers.
     */
    @GET
    @Path("/config")
    public Response getConfig() {
        Map<String, Object> config = Map.of(
            "maxHeroes", applicationConfig.getMaxHeroes(),
            "maxVillains", applicationConfig.getMaxVillains(),
            "powerThreshold", applicationConfig.getPowerThreshold()
        );
        return Response.ok(config).build();
    }
    
    /**
     * Compara héroes y villanos poderosos.
     */
    @GET
    @Path("/comparison")
    public Response getComparison() {
        Map<String, Object> comparison = powerAnalysisService.comparePowerful();
        return Response.ok(comparison).build();
    }
}

