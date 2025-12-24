package com.jakartaee.nosql.resource;

import com.jakartaee.nosql.entity.HeroMongo;
import com.jakartaee.nosql.entity.VillainMongo;
import com.jakartaee.nosql.service.NoSQLService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Recurso REST que demuestra operaciones con MongoDB usando Panache MongoDB.
 * Muestra características específicas de NoSQL.
 */
@Path("/api/nosql")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoSQLResource {
    
    @Inject
    NoSQLService noSQLService;
    
    /**
     * Obtiene todos los héroes.
     * GET /api/nosql/heroes
     */
    @GET
    @Path("/heroes")
    public Response getAllHeroes() {
        List<HeroMongo> heroes = noSQLService.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Obtiene un héroe por ID.
     * GET /api/nosql/heroes/{id}
     */
    @GET
    @Path("/heroes/{id}")
    public Response getHero(@PathParam("id") String id) {
        ObjectId objectId = new ObjectId(id);
        Optional<HeroMongo> hero = noSQLService.getHeroById(objectId);
        return hero.map(h -> Response.ok(h).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Hero not found"))
                        .build());
    }
    
    /**
     * Crea un héroe usando Active Record Pattern.
     * POST /api/nosql/heroes
     */
    @POST
    @Path("/heroes")
    public Response createHero(HeroMongo hero) {
        HeroMongo created = noSQLService.createHero(hero);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    /**
     * Busca héroes poderosos.
     * GET /api/nosql/heroes/powerful?minLevel=80
     */
    @GET
    @Path("/heroes/powerful")
    public Response getPowerfulHeroes(@QueryParam("minLevel") @DefaultValue("80") int minLevel) {
        List<HeroMongo> heroes = noSQLService.findPowerfulHeroes(minLevel);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes activos.
     * GET /api/nosql/heroes/active
     */
    @GET
    @Path("/heroes/active")
    public Response getActiveHeroes() {
        List<HeroMongo> heroes = noSQLService.findActiveHeroes();
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes por nombre (búsqueda parcial).
     * GET /api/nosql/heroes/search?name=Super
     */
    @GET
    @Path("/heroes/search")
    public Response searchHeroes(@QueryParam("name") String name) {
        List<HeroMongo> heroes = noSQLService.findHeroesByName(name);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes que tengan una habilidad específica.
     * Demuestra queries en arrays de MongoDB.
     * GET /api/nosql/heroes/by-ability?ability=Flight
     */
    @GET
    @Path("/heroes/by-ability")
    public Response getHeroesByAbility(@QueryParam("ability") String ability) {
        List<HeroMongo> heroes = noSQLService.findHeroesByAbility(ability);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes por ciudad.
     * Demuestra queries en documentos anidados.
     * GET /api/nosql/heroes/by-city?city=Metropolis
     */
    @GET
    @Path("/heroes/by-city")
    public Response getHeroesByCity(@QueryParam("city") String city) {
        List<HeroMongo> heroes = noSQLService.findHeroesByCity(city);
        return Response.ok(heroes).build();
    }
    
    /**
     * Busca héroes con misiones pendientes.
     * Demuestra queries en arrays de documentos anidados.
     * GET /api/nosql/heroes/with-pending-missions
     */
    @GET
    @Path("/heroes/with-pending-missions")
    public Response getHeroesWithPendingMissions() {
        List<HeroMongo> heroes = noSQLService.findHeroesWithPendingMissions();
        return Response.ok(heroes).build();
    }
    
    /**
     * Agrega una habilidad a un héroe.
     * Demuestra actualización de arrays en MongoDB.
     * PUT /api/nosql/heroes/{id}/abilities
     */
    @PUT
    @Path("/heroes/{id}/abilities")
    public Response addAbility(@PathParam("id") String id, Map<String, String> request) {
        ObjectId objectId = new ObjectId(id);
        String ability = request.get("ability");
        HeroMongo hero = noSQLService.addAbilityToHero(objectId, ability);
        if (hero != null) {
            return Response.ok(hero).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Hero not found"))
                    .build();
        }
    }
    
    /**
     * Agrega una misión a un héroe.
     * Demuestra actualización de arrays de documentos anidados.
     * POST /api/nosql/heroes/{id}/missions
     */
    @POST
    @Path("/heroes/{id}/missions")
    public Response addMission(@PathParam("id") String id, HeroMongo.Mission mission) {
        ObjectId objectId = new ObjectId(id);
        HeroMongo hero = noSQLService.addMissionToHero(objectId, mission);
        if (hero != null) {
            return Response.ok(hero).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Hero not found"))
                    .build();
        }
    }
    
    /**
     * Actualiza un héroe.
     * PUT /api/nosql/heroes/{id}
     */
    @PUT
    @Path("/heroes/{id}")
    public Response updateHero(@PathParam("id") String id, HeroMongo heroUpdate) {
        ObjectId objectId = new ObjectId(id);
        HeroMongo updated = noSQLService.updateHero(objectId, heroUpdate);
        if (updated != null) {
            return Response.ok(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Hero not found"))
                    .build();
        }
    }
    
    /**
     * Elimina un héroe.
     * DELETE /api/nosql/heroes/{id}
     */
    @DELETE
    @Path("/heroes/{id}")
    public Response deleteHero(@PathParam("id") String id) {
        ObjectId objectId = new ObjectId(id);
        noSQLService.deleteHero(objectId);
        return Response.noContent().build();
    }
    
    /**
     * Crea un villano.
     * POST /api/nosql/villains
     */
    @POST
    @Path("/villains")
    public Response createVillain(VillainMongo villain) {
        VillainMongo created = noSQLService.createVillain(villain);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    /**
     * Busca villanos peligrosos.
     * GET /api/nosql/villains/dangerous
     */
    @GET
    @Path("/villains/dangerous")
    public Response getDangerousVillains() {
        List<VillainMongo> villains = noSQLService.findDangerousVillains();
        return Response.ok(villains).build();
    }
    
    /**
     * Busca villanos por nivel de amenaza.
     * GET /api/nosql/villains/threat-level/{level}
     */
    @GET
    @Path("/villains/threat-level/{level}")
    public Response getVillainsByThreatLevel(@PathParam("level") String threatLevel) {
        List<VillainMongo> villains = noSQLService.findVillainsByThreatLevel(threatLevel);
        return Response.ok(villains).build();
    }
    
    /**
     * Obtiene estadísticas de héroes.
     * GET /api/nosql/heroes/statistics
     */
    @GET
    @Path("/heroes/statistics")
    public Response getHeroStatistics() {
        Map<String, Object> stats = noSQLService.getHeroStatistics();
        return Response.ok(stats).build();
    }
    
    /**
     * Información sobre características de MongoDB/NoSQL demostradas.
     * GET /api/nosql/info
     */
    @GET
    @Path("/info")
    public Response getInfo() {
        return Response.ok(Map.of(
            "features", List.of(
                "Panache MongoDB - Active Record Pattern",
                "Panache MongoDB - Repository Pattern",
                "Documentos anidados",
                "Arrays de valores simples",
                "Arrays de documentos anidados",
                "Queries flexibles sin esquema fijo",
                "ObjectId como identificador",
                "Dev Services para MongoDB automático"
            ),
            "mongodbFeatures", Map.of(
                "Nested Documents", "Documentos dentro de documentos",
                "Arrays", "Arrays de valores y documentos",
                "Flexible Schema", "Sin esquema fijo, documentos pueden variar",
                "ObjectId", "Identificador único de MongoDB",
                "Queries", "Queries en documentos anidados y arrays"
            ),
            "devServices", "MongoDB se inicia automáticamente en modo desarrollo"
        )).build();
    }
}

