package com.jakartaee.jpa.resource;

import com.jakartaee.jpa.dao.HeroDAO;
import com.jakartaee.jpa.entity.HeroJPA;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Recurso que demuestra características específicas de JPA y EntityManager.
 * Muestra métodos avanzados del EntityManager.
 */
@Path("/api/jpa-demo")
@Produces(MediaType.APPLICATION_JSON)
public class JPADemoResource {
    
    @Inject
    EntityManager entityManager;
    
    @Inject
    HeroDAO heroDAO;
    
    /**
     * Demuestra diferentes métodos del EntityManager.
     * GET /api/jpa-demo/entity-manager-methods/{id}
     */
    @GET
    @Path("/entity-manager-methods/{id}")
    public Response demonstrateEntityManagerMethods(@PathParam("id") Long id) {
        Map<String, Object> results = new HashMap<>();
        
        // 1. find() - Buscar entidad
        HeroJPA hero1 = entityManager.find(HeroJPA.class, id);
        results.put("find", hero1 != null ? hero1.getName() : "Not found");
        
        if (hero1 != null) {
            // 2. contains() - Verificar si está gestionada
            results.put("isManaged", entityManager.contains(hero1));
            
            // 3. detach() - Desvincular del contexto
            entityManager.detach(hero1);
            results.put("isManagedAfterDetach", entityManager.contains(hero1));
            
            // 4. getReference() - Obtener referencia lazy
            HeroJPA hero2 = entityManager.getReference(HeroJPA.class, id);
            results.put("getReference", hero2 != null ? "Reference obtained" : "Not found");
            
            // 5. refresh() - Refrescar desde BD
            HeroJPA hero3 = entityManager.find(HeroJPA.class, id);
            if (hero3 != null) {
                entityManager.refresh(hero3);
                results.put("refresh", "Entity refreshed");
            }
        }
        
        return Response.ok(results).build();
    }
    
    /**
     * Demuestra el uso de flush() para sincronizar con la BD.
     * GET /api/jpa-demo/flush-demo
     */
    @GET
    @Path("/flush-demo")
    public Response demonstrateFlush() {
        Map<String, String> results = new HashMap<>();
        results.put("message", "Flush forces synchronization with database");
        results.put("usage", "entityManager.flush()");
        results.put("note", "Changes are written to DB but transaction is not committed");
        return Response.ok(results).build();
    }
    
    /**
     * Demuestra el uso de clear() para limpiar el contexto de persistencia.
     * GET /api/jpa-demo/clear-demo
     */
    @GET
    @Path("/clear-demo")
    public Response demonstrateClear() {
        Map<String, String> results = new HashMap<>();
        results.put("message", "Clear detaches all entities from persistence context");
        results.put("usage", "entityManager.clear()");
        results.put("note", "Useful for freeing memory in long-running transactions");
        return Response.ok(results).build();
    }
    
    /**
     * Información sobre características de JPA demostradas.
     * GET /api/jpa-demo/info
     */
    @GET
    @Path("/info")
    public Response getJPAInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("jpaFeatures", java.util.List.of(
            "EntityManager operations (persist, find, merge, remove)",
            "Named Queries (@NamedQuery)",
            "JPQL queries (createQuery)",
            "Criteria API (type-safe queries)",
            "Relationships (OneToMany, ManyToOne)",
            "Lifecycle callbacks (@PrePersist, @PreUpdate, @PostLoad)",
            "Optimistic locking (@Version)",
            "Lazy/Eager loading",
            "Cascade operations",
            "EntityManager methods (flush, refresh, detach, clear)"
        ));
        info.put("entityManagerInjection", java.util.List.of(
            "@Inject EntityManager (CDI - recommended in Quarkus)",
            "@PersistenceContext EntityManager (JPA standard)"
        ));
        return Response.ok(info).build();
    }
}

