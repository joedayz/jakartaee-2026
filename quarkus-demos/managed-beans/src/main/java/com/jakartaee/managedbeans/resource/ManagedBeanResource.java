package com.jakartaee.managedbeans.resource;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.common.entities.Villain;
import com.jakartaee.managedbeans.bean.HeroManagedBean;
import com.jakartaee.managedbeans.bean.VillainManagedBean;
import com.jakartaee.managedbeans.service.BattleService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

/**
 * Recurso REST que demuestra el uso de Jakarta Managed Beans.
 * 
 * Este recurso muestra cómo inyectar y usar Managed Beans en endpoints REST.
 */
@Path("/api/managed-beans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ManagedBeanResource {
    
    @Inject
    HeroManagedBean heroManagedBean;
    
    @Inject
    VillainManagedBean villainManagedBean;
    
    @Inject
    BattleService battleService;
    
    // ========== Endpoints para Héroes ==========
    
    @GET
    @Path("/heroes")
    public Response getAllHeroes() {
        List<Hero> heroes = heroManagedBean.getAllHeroes();
        return Response.ok(heroes).build();
    }
    
    @GET
    @Path("/heroes/{id}")
    public Response getHeroById(@PathParam("id") Long id) {
        return heroManagedBean.getHeroById(id)
                .map(hero -> Response.ok(hero).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Hero not found with id: " + id)
                        .build());
    }
    
    @POST
    @Path("/heroes")
    public Response createHero(@QueryParam("name") String name,
                               @QueryParam("power") String power,
                               @QueryParam("powerLevel") Integer powerLevel) {
        Hero hero = heroManagedBean.createHero(name, power, powerLevel);
        return Response.status(Response.Status.CREATED).entity(hero).build();
    }
    
    @PUT
    @Path("/heroes/{id}")
    public Response updateHero(@PathParam("id") Long id,
                               @QueryParam("name") String name,
                               @QueryParam("power") String power,
                               @QueryParam("powerLevel") Integer powerLevel) {
        Hero hero = heroManagedBean.updateHero(id, name, power, powerLevel);
        if (hero != null) {
            return Response.ok(hero).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Hero not found with id: " + id)
                .build();
    }
    
    @DELETE
    @Path("/heroes/{id}")
    public Response deleteHero(@PathParam("id") Long id) {
        boolean deleted = heroManagedBean.deleteHero(id);
        if (deleted) {
            return Response.ok(Map.of("message", "Hero deleted successfully", "id", id)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Hero not found with id: " + id)
                .build();
    }
    
    // ========== Endpoints para Villanos ==========
    
    @GET
    @Path("/villains")
    public Response getAllVillains() {
        List<Villain> villains = villainManagedBean.getAllVillains();
        return Response.ok(villains).build();
    }
    
    @GET
    @Path("/villains/{id}")
    public Response getVillainById(@PathParam("id") Long id) {
        return villainManagedBean.getVillainById(id)
                .map(villain -> Response.ok(villain).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Villain not found with id: " + id)
                        .build());
    }
    
    @POST
    @Path("/villains")
    public Response createVillain(@QueryParam("name") String name,
                                   @QueryParam("power") String power,
                                   @QueryParam("powerLevel") Integer powerLevel) {
        Villain villain = villainManagedBean.createVillain(name, power, powerLevel);
        return Response.status(Response.Status.CREATED).entity(villain).build();
    }
    
    // ========== Endpoints para Batallas ==========
    
    @POST
    @Path("/battles")
    public Response simulateBattle(@QueryParam("heroId") Long heroId,
                                    @QueryParam("villainId") Long villainId) {
        Map<String, Object> battleResult = battleService.simulateBattle(heroId, villainId);
        return Response.ok(battleResult).build();
    }
    
    // ========== Endpoints de Información ==========
    
    @GET
    @Path("/stats")
    public Response getStats() {
        Map<String, Object> stats = battleService.getStats();
        return Response.ok(stats).build();
    }
    
    @GET
    @Path("/hero-stats")
    public Response getHeroBeanStats() {
        return Response.ok(heroManagedBean.getStats()).build();
    }
    
    @GET
    @Path("/info")
    public Response getManagedBeanInfo() {
        Map<String, Object> info = Map.of(
                "description", "Jakarta Managed Beans Demo",
                "features", List.of(
                        "@ManagedBean annotation",
                        "@PostConstruct lifecycle",
                        "@PreDestroy lifecycle",
                        "Dependency injection with @Inject",
                        "Multiple managed beans",
                        "Bean statistics tracking"
                ),
                "managedBeans", List.of(
                        "HeroManagedBean - Gestiona héroes",
                        "VillainManagedBean - Gestiona villanos",
                        "BattleService - Coordina batallas"
                ),
                "note", "Managed Beans son parte del Jakarta EE Core Profile 11"
        );
        return Response.ok(info).build();
    }
}

