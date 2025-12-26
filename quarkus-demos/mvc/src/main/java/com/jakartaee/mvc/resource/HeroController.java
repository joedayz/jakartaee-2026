package com.jakartaee.mvc.resource;

import com.jakartaee.common.entities.Hero;
import com.jakartaee.mvc.service.HeroService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador MVC para gestionar héroes.
 * 
 * NOTA: Este demo usa Qute (motor de plantillas de Quarkus) como alternativa
 * a Jakarta MVC, ya que Quarkus NO implementa Jakarta MVC estándar.
 */
@Path("/heroes")
public class HeroController {
    
    @Inject
    HeroService heroService;
    
    @Inject
    Template heroes; // heroes.html
    
    @Inject
    Template heroForm; // hero-form.html
    
    @Inject
    Template heroDetail; // hero-detail.html
    
    /**
     * Listar todos los héroes (GET /heroes)
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listHeroes() {
        List<Hero> heroesList = heroService.getAllHeroes();
        return heroes.data("heroes", heroesList);
    }
    
    /**
     * Mostrar formulario para crear nuevo héroe (GET /heroes/new)
     */
    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showCreateForm() {
        return heroForm.data("hero", new Hero())
                      .data("action", "create")
                      .data("title", "Crear Nuevo Héroe");
    }
    
    /**
     * Crear nuevo héroe (POST /heroes)
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createHero(@FormParam("name") String name,
                              @FormParam("power") String power,
                              @FormParam("powerLevel") Integer powerLevel,
                              @FormParam("description") String description) {
        Hero hero = heroService.createHero(name, power, powerLevel);
        if (description != null && !description.trim().isEmpty()) {
            hero.setDescription(description);
        }
        return Response.seeOther(URI.create("/heroes")).build();
    }
    
    /**
     * Ver detalles de un héroe (GET /heroes/{id})
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance viewHero(@PathParam("id") Long id) {
        Hero hero = heroService.getHeroById(id)
                .orElseThrow(() -> new NotFoundException("Hero not found with id: " + id));
        return heroDetail.data("hero", hero);
    }
    
    /**
     * Mostrar formulario para editar héroe (GET /heroes/{id}/edit)
     */
    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showEditForm(@PathParam("id") Long id) {
        Hero hero = heroService.getHeroById(id)
                .orElseThrow(() -> new NotFoundException("Hero not found with id: " + id));
        return heroForm.data("hero", hero)
                      .data("action", "update")
                      .data("title", "Editar Héroe");
    }
    
    /**
     * Actualizar héroe (POST /heroes/{id})
     */
    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateHero(@PathParam("id") Long id,
                               @FormParam("name") String name,
                               @FormParam("power") String power,
                               @FormParam("powerLevel") Integer powerLevel,
                               @FormParam("description") String description) {
        Hero hero = heroService.updateHero(id, name, power, powerLevel);
        if (hero != null && description != null) {
            hero.setDescription(description);
        }
        return Response.seeOther(URI.create("/heroes/" + id)).build();
    }
    
    /**
     * Eliminar héroe (POST /heroes/{id}/delete)
     */
    @POST
    @Path("/{id}/delete")
    public Response deleteHero(@PathParam("id") Long id) {
        heroService.deleteHero(id);
        return Response.seeOther(URI.create("/heroes")).build();
    }
}

