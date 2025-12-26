package com.jakartaee.mvc.resource;

import com.jakartaee.common.entities.Villain;
import com.jakartaee.mvc.service.VillainService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

/**
 * Controlador MVC para gestionar villanos.
 * 
 * NOTA: Este demo usa Qute (motor de plantillas de Quarkus) como alternativa
 * a Jakarta MVC, ya que Quarkus NO implementa Jakarta MVC estándar.
 */
@Path("/villains")
public class VillainController {
    
    @Inject
    VillainService villainService;
    
    @Inject
    Template villains; // villains.html
    
    @Inject
    Template villainForm; // villain-form.html
    
    @Inject
    Template villainDetail; // villain-detail.html
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listVillains() {
        return villains.data("villains", villainService.getAllVillains());
    }
    
    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showCreateForm() {
        return villainForm.data("villain", new Villain())
                         .data("action", "create")
                         .data("title", "Crear Nuevo Villano");
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createVillain(@FormParam("name") String name,
                                  @FormParam("power") String power,
                                  @FormParam("powerLevel") Integer powerLevel,
                                  @FormParam("description") String description) {
        Villain villain = villainService.createVillain(name, power, powerLevel);
        if (description != null && !description.trim().isEmpty()) {
            villain.setDescription(description);
        }
        return Response.seeOther(URI.create("/villains")).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance viewVillain(@PathParam("id") Long id) {
        Villain villain = villainService.getVillainById(id)
                .orElseThrow(() -> new NotFoundException("Villain not found with id: " + id));
        return villainDetail.data("villain", villain);
    }
    
    @GET
    @Path("/{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance showEditForm(@PathParam("id") Long id) {
        Villain villain = villainService.getVillainById(id)
                .orElseThrow(() -> new NotFoundException("Villain not found with id: " + id));
        return villainForm.data("villain", villain)
                         .data("action", "update")
                         .data("title", "Editar Villano");
    }
    
    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateVillain(@PathParam("id") Long id,
                                  @FormParam("name") String name,
                                  @FormParam("power") String power,
                                  @FormParam("powerLevel") Integer powerLevel,
                                  @FormParam("description") String description) {
        Villain villain = villainService.updateVillain(id, name, power, powerLevel);
        if (villain != null && description != null) {
            villain.setDescription(description);
        }
        return Response.seeOther(URI.create("/villains/" + id)).build();
    }
    
    @POST
    @Path("/{id}/delete")
    public Response deleteVillain(@PathParam("id") Long id) {
        villainService.deleteVillain(id);
        return Response.seeOther(URI.create("/villains")).build();
    }
}

