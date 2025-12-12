package com.jakartaee.annotations.resource;

import com.jakartaee.annotations.annotation.HeroPower;
import com.jakartaee.annotations.annotation.Loggable;
import com.jakartaee.annotations.service.HeroService;
import com.jakartaee.annotations.service.PowerService;
import com.jakartaee.common.entities.Hero;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Recurso REST que demuestra el uso de anotaciones personalizadas
 * en el contexto de gestión de héroes.
 */
@Path("/api/heroes")
@Produces(MediaType.APPLICATION_JSON)
public class HeroAnnotationsResource {
    
    @Inject
    HeroService heroService;
    
    @Inject
    PowerService powerService;
    
    /**
     * Listar héroes usando métodos con anotaciones personalizadas.
     */
    @GET
    @Path("/annotated")
    @HeroPower(category = "INFO", description = "Listar héroes usando anotaciones")
    @Loggable(level = "INFO")
    public Response getAnnotatedHeroes() {
        List<Hero> allHeroes = heroService.getAllHeroes();
        List<Hero> powerfulHeroes = heroService.getPowerfulHeroes();
        
        Map<String, Object> result = new HashMap<>();
        result.put("allHeroes", allHeroes);
        result.put("powerfulHeroes", powerfulHeroes);
        result.put("powerfulHeroesCount", powerfulHeroes.size());
        result.put("note", "Los héroes poderosos se obtienen usando @HeroPower(minLevel=80)");
        
        return Response.ok(result).build();
    }
    
    /**
     * Validar un héroe usando anotaciones personalizadas.
     */
    @POST
    @Path("/validate")
    @Loggable(level = "INFO", includeParameters = true, includeResult = true)
    public Response validateHero(Hero hero) {
        Map<String, Object> validation = new HashMap<>();
        
        if (hero == null) {
            validation.put("valid", false);
            validation.put("errors", List.of("El héroe no puede ser nulo"));
            return Response.status(Response.Status.BAD_REQUEST).entity(validation).build();
        }
        
        List<String> errors = new java.util.ArrayList<>();
        
        // Validar nombre
        if (hero.getName() == null || hero.getName().trim().isEmpty()) {
            errors.add("El nombre del héroe es requerido");
        }
        
        // Validar poder
        if (hero.getPower() == null || hero.getPower().trim().isEmpty()) {
            errors.add("El poder del héroe es requerido");
        }
        
        // Validar nivel de poder usando lógica similar a @PowerLevel
        if (hero.getPowerLevel() == null) {
            errors.add("El nivel de poder es requerido");
        } else if (hero.getPowerLevel() < 1 || hero.getPowerLevel() > 100) {
            errors.add("El nivel de poder debe estar entre 1 y 100");
        }
        
        validation.put("valid", errors.isEmpty());
        validation.put("errors", errors);
        
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validation).build();
        }
        
        // Categorizar poder usando método con anotación personalizada
        String category = powerService.categorizePower(hero);
        boolean isCompetent = powerService.isCompetentHero(hero);
        
        validation.put("hero", hero);
        validation.put("powerCategory", category);
        validation.put("isCompetent", isCompetent);
        validation.put("note", "Validación realizada usando métodos con anotaciones personalizadas");
        
        return Response.ok(validation).build();
    }
    
    /**
     * Obtener información sobre un héroe específico.
     */
    @GET
    @Path("/{name}")
    @Loggable(level = "DEBUG", includeParameters = true)
    public Response getHeroByName(@PathParam("name") String name) {
        Hero hero = heroService.getHeroByName(name);
        
        if (hero == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Héroe no encontrado: " + name))
                    .build();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("hero", hero);
        result.put("powerCategory", powerService.categorizePower(hero));
        result.put("isCompetent", powerService.isCompetentHero(hero));
        
        return Response.ok(result).build();
    }
}

