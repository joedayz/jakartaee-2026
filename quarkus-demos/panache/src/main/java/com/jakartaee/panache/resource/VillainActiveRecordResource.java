package com.jakartaee.panache.resource;

import com.jakartaee.panache.entity.VillainEntity;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

/**
 * Recurso REST que demuestra el uso de Panache Active Record Pattern.
 * Usa métodos directamente en VillainEntity (que extiende PanacheEntity).
 */
@Path("/api/villains/activerecord")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VillainActiveRecordResource {
    
    /**
     * Listar todos los villanos usando Active Record Pattern
     * Usa el método estático findAll() directamente en la entidad
     */
    @GET
    public Response getAllVillains() {
        List<VillainEntity> villains = VillainEntity.listAll();
        return Response.ok(Map.of(
            "villains", villains,
            "count", villains.size(),
            "pattern", "Active Record Pattern",
            "note", "Usando VillainEntity.listAll() directamente"
        )).build();
    }
    
    /**
     * Obtener villano por ID usando Active Record Pattern
     */
    @GET
    @Path("/{id}")
    public Response getVillainById(@PathParam("id") Long id) {
        VillainEntity villain = VillainEntity.findById(id);
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado con ID: " + id))
                    .build();
        }
        
        return Response.ok(Map.of(
            "villain", villain,
            "pattern", "Active Record Pattern",
            "note", "Usando VillainEntity.findById() directamente"
        )).build();
    }
    
    /**
     * Buscar villanos poderosos usando método estático personalizado
     */
    @GET
    @Path("/powerful")
    public Response getPowerfulVillains() {
        List<VillainEntity> villains = VillainEntity.findPowerful();
        return Response.ok(Map.of(
            "villains", villains,
            "count", villains.size(),
            "pattern", "Active Record Pattern",
            "note", "Usando método estático personalizado VillainEntity.findPowerful()"
        )).build();
    }
    
    /**
     * Buscar villanos activos usando método estático personalizado
     */
    @GET
    @Path("/active")
    public Response getActiveVillains() {
        List<VillainEntity> villains = VillainEntity.findActive();
        return Response.ok(villains).build();
    }
    
    /**
     * Buscar villanos por nivel de amenaza
     */
    @GET
    @Path("/threat/{level}")
    public Response getVillainsByThreatLevel(@PathParam("level") String threatLevel) {
        List<VillainEntity> villains = VillainEntity.findByThreatLevel(threatLevel);
        return Response.ok(villains).build();
    }
    
    /**
     * Buscar villano por nombre usando Panache Query
     */
    @GET
    @Path("/search")
    public Response searchVillainByName(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El parámetro 'name' es requerido"))
                    .build();
        }
        
        VillainEntity villain = VillainEntity.find("name", name).firstResult();
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado: " + name))
                    .build();
        }
        
        return Response.ok(villain).build();
    }
    
    /**
     * Crear villano usando Active Record Pattern
     * Usa el método persist() directamente en la instancia
     */
    @POST
    @Transactional
    public Response createVillain(VillainEntity villain) {
        if (villain == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "El villano no puede ser nulo"))
                    .build();
        }
        
        // Verificar si ya existe
        VillainEntity existing = VillainEntity.find("name", villain.name).firstResult();
        if (existing != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Ya existe un villano con el nombre: " + villain.name))
                    .build();
        }
        
        villain.persist();
        
        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                    "villain", villain,
                    "pattern", "Active Record Pattern",
                    "note", "Villano creado usando villain.persist() directamente en la instancia"
                ))
                .build();
    }
    
    /**
     * Actualizar villano usando Active Record Pattern
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateVillain(@PathParam("id") Long id, VillainEntity villainUpdate) {
        VillainEntity villain = VillainEntity.findById(id);
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado con ID: " + id))
                    .build();
        }
        
        // Actualizar campos
        if (villainUpdate.name != null) villain.name = villainUpdate.name;
        if (villainUpdate.power != null) villain.power = villainUpdate.power;
        if (villainUpdate.powerLevel != null) villain.powerLevel = villainUpdate.powerLevel;
        if (villainUpdate.description != null) villain.description = villainUpdate.description;
        if (villainUpdate.threatLevel != null) villain.threatLevel = villainUpdate.threatLevel;
        if (villainUpdate.isActive != null) villain.isActive = villainUpdate.isActive;
        
        villain.persist();
        
        return Response.ok(Map.of(
            "villain", villain,
            "pattern", "Active Record Pattern",
            "note", "Villano actualizado usando villain.persist() directamente"
        )).build();
    }
    
    /**
     * Eliminar villano usando Active Record Pattern
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteVillain(@PathParam("id") Long id) {
        VillainEntity villain = VillainEntity.findById(id);
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado con ID: " + id))
                    .build();
        }
        
        villain.delete();
        
        return Response.ok(Map.of(
            "message", "Villano eliminado exitosamente",
            "id", id,
            "pattern", "Active Record Pattern",
            "note", "Villano eliminado usando villain.delete() directamente"
        )).build();
    }
    
    /**
     * Activar villano usando método de instancia personalizado
     */
    @POST
    @Path("/{id}/activate")
    @Transactional
    public Response activateVillain(@PathParam("id") Long id) {
        VillainEntity villain = VillainEntity.findById(id);
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado con ID: " + id))
                    .build();
        }
        
        villain.activate();
        
        return Response.ok(Map.of(
            "villain", villain,
            "pattern", "Active Record Pattern",
            "note", "Villano activado usando método de instancia villain.activate()"
        )).build();
    }
    
    /**
     * Desactivar villano usando método de instancia personalizado
     */
    @POST
    @Path("/{id}/deactivate")
    @Transactional
    public Response deactivateVillain(@PathParam("id") Long id) {
        VillainEntity villain = VillainEntity.findById(id);
        
        if (villain == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Villano no encontrado con ID: " + id))
                    .build();
        }
        
        villain.deactivate();
        
        return Response.ok(Map.of(
            "villain", villain,
            "pattern", "Active Record Pattern",
            "note", "Villano desactivado usando método de instancia villain.deactivate()"
        )).build();
    }
    
    /**
     * Estadísticas usando Active Record Pattern
     */
    @GET
    @Path("/stats")
    public Response getStats() {
        long totalVillains = VillainEntity.count();
        long powerfulVillains = VillainEntity.countPowerful();
        long activeVillains = VillainEntity.count("isActive", true);
        
        return Response.ok(Map.of(
            "totalVillains", totalVillains,
            "powerfulVillains", powerfulVillains,
            "activeVillains", activeVillains,
            "pattern", "Active Record Pattern",
            "note", "Estadísticas obtenidas usando métodos estáticos directamente en la entidad"
        )).build();
    }
}

