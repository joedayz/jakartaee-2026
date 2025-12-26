package com.jakartaee.renarde.controller;

import com.jakartaee.common.entities.Villain;
import io.quarkiverse.renarde.Controller;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;

import java.util.List;

/**
 * Controlador Renarde para gestionar villanos.
 * 
 * Demuestra las mismas convenciones y utilidades de Renarde
 * aplicadas a otra entidad.
 */
public class Villains extends Controller {
    
    @Inject
    EntityManager entityManager;
    
    public void list() {
        List<Villain> villains = entityManager.createQuery(
                "SELECT v FROM Villain v ORDER BY v.name", Villain.class)
                .getResultList();
        render("list", villains);
    }
    
    public void view(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain == null) {
            notFound();
            return;
        }
        render("view", villain);
    }
    
    public void newVillain() {
        render("form", new Villain(), "create");
    }
    
    @Transactional
    public void create(@BeanParam @Valid Villain villain) {
        if (validationFailed()) {
            render("form", villain, "create");
            return;
        }
        
        entityManager.persist(villain);
        entityManager.flush();
        
        flash("success", "Villano creado exitosamente: " + villain.getName());
        redirect(Villains.class).list();
    }
    
    public void edit(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain == null) {
            notFound();
            return;
        }
        render("form", villain, "update");
    }
    
    @Transactional
    public void update(Long id, @BeanParam @Valid Villain villain) {
        if (validationFailed()) {
            render("form", villain, "update");
            return;
        }
        
        Villain existingVillain = entityManager.find(Villain.class, id);
        if (existingVillain == null) {
            notFound();
            return;
        }
        
        existingVillain.setName(villain.getName());
        existingVillain.setPower(villain.getPower());
        existingVillain.setPowerLevel(villain.getPowerLevel());
        if (villain.getDescription() != null) {
            existingVillain.setDescription(villain.getDescription());
        }
        
        entityManager.merge(existingVillain);
        
        flash("success", "Villano actualizado exitosamente: " + existingVillain.getName());
        redirect(Villains.class).view(id);
    }
    
    @Transactional
    public void delete(Long id) {
        Villain villain = entityManager.find(Villain.class, id);
        if (villain == null) {
            notFound();
            return;
        }
        
        String villainName = villain.getName();
        entityManager.remove(villain);
        
        flash("success", "Villano eliminado: " + villainName);
        redirect(Villains.class).list();
    }
}

