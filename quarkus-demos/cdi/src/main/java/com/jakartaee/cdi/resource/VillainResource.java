package com.jakartaee.cdi.resource;

import com.jakartaee.cdi.qualifier.VillainQualifier;
import com.jakartaee.cdi.service.VillainService;
import com.jakartaee.common.entities.Villain;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * Recurso REST para gestionar Villains.
 * Demuestra inyección de dependencias con qualifiers.
 */
@Path("/api/villains")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VillainResource {
    
    @Inject
    @VillainQualifier
    VillainService villainService;
    
    @GET
    public Response getAllVillains() {
        List<Villain> villains = villainService.getAllVillains();
        return Response.ok(villains).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getVillain(@PathParam("id") Long id) {
        Optional<Villain> villain = villainService.getVillainById(id);
        return villain.map(v -> Response.ok(v).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Villain not found with id: " + id)
                        .build());
    }
    
    @GET
    @Path("/search")
    public Response searchVillains(@QueryParam("name") String name) {
        if (name == null || name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name parameter is required")
                    .build();
        }
        List<Villain> villains = villainService.findVillainsByName(name);
        return Response.ok(villains).build();
    }
    
    @GET
    @Path("/dangerous")
    public Response getDangerousVillains() {
        List<Villain> villains = villainService.getDangerousVillains();
        return Response.ok(villains).build();
    }
    
    @POST
    public Response createVillain(Villain villain) {
        Villain created = villainService.createVillain(villain);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateVillain(@PathParam("id") Long id, Villain villain) {
        Optional<Villain> existing = villainService.getVillainById(id);
        if (existing.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Villain not found with id: " + id)
                    .build();
        }
        villain.setId(id);
        Villain updated = villainService.updateVillain(villain);
        return Response.ok(updated).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteVillain(@PathParam("id") Long id) {
        Optional<Villain> villain = villainService.getVillainById(id);
        if (villain.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Villain not found with id: " + id)
                    .build();
        }
        villainService.deleteVillain(id);
        return Response.noContent().build();
    }
}

