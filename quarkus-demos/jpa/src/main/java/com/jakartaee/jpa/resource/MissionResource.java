package com.jakartaee.jpa.resource;

import com.jakartaee.jpa.entity.Mission;
import com.jakartaee.jpa.entity.Mission.MissionStatus;
import com.jakartaee.jpa.service.HeroService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Recurso REST para Missions que demuestra relaciones JPA.
 */
@Path("/api/missions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MissionResource {
    
    @Inject
    HeroService heroService;
    
    /**
     * Obtiene todas las misiones de un héroe usando Named Query.
     * GET /api/missions/hero/{heroId}
     */
    @GET
    @Path("/hero/{heroId}")
    public Response getHeroMissions(@PathParam("heroId") Long heroId) {
        List<Mission> missions = heroService.getHeroMissions(heroId);
        return Response.ok(missions).build();
    }
    
    /**
     * Crea una nueva misión para un héroe.
     * Demuestra operaciones con relaciones ManyToOne.
     * POST /api/missions
     */
    @POST
    public Response createMission(CreateMissionRequest request) {
        try {
            Mission mission = heroService.createMission(
                    request.getHeroId(),
                    request.getTitle(),
                    request.getDescription());
            return Response.status(Response.Status.CREATED).entity(mission).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    /**
     * Completa una misión.
     * PUT /api/missions/{id}/complete
     */
    @PUT
    @Path("/{id}/complete")
    public Response completeMission(@PathParam("id") Long id) {
        Mission mission = heroService.completeMission(id);
        if (mission != null) {
            return Response.ok(mission).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Mission not found with id: " + id)
                    .build();
        }
    }
    
    /**
     * Obtiene estadísticas de misiones por héroe usando JPQL con GROUP BY.
     * GET /api/missions/statistics
     */
    @GET
    @Path("/statistics")
    public Response getMissionStatistics() {
        List<Map<String, Object>> stats = heroService.getMissionStatisticsByHero();
        return Response.ok(stats).build();
    }
    
    /**
     * DTO para crear una misión.
     */
    public static class CreateMissionRequest {
        private Long heroId;
        private String title;
        private String description;
        
        public Long getHeroId() {
            return heroId;
        }
        
        public void setHeroId(Long heroId) {
            this.heroId = heroId;
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }
    }
}

