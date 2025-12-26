package com.jakartaee.renarde.controller;

import com.jakartaee.common.entities.Hero;
import io.quarkiverse.renarde.Controller;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;

import java.util.List;

/**
 * Controlador Renarde para gestionar héroes.
 * 
 * Renarde proporciona convenciones automáticas:
 * - Métodos públicos sin parámetros → GET /heroes/{methodName}
 * - Métodos públicos con parámetros → POST /heroes/{methodName}
 * - Templates automáticos en templates/Heroes/{methodName}.html
 */
public class Heroes extends Controller {
    
    @Inject
    EntityManager entityManager;
    
    /**
     * Listar todos los héroes.
     * GET /heroes/list
     * Template: templates/Heroes/list.html
     */
    public void list() {
        List<Hero> heroes = entityManager.createQuery(
                "SELECT h FROM Hero h ORDER BY h.name", Hero.class)
                .getResultList();
        render("list", heroes);
    }
    
    /**
     * Ver detalles de un héroe.
     * GET /heroes/view/{id}
     * Template: templates/Heroes/view.html
     */
    public void view(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero == null) {
            notFound();
            return;
        }
        render("view", hero);
    }
    
    /**
     * Mostrar formulario para crear nuevo héroe.
     * GET /heroes/new
     * Template: templates/Heroes/form.html
     */
    public void newHero() {
        render("form", new Hero(), "create");
    }
    
    /**
     * Crear nuevo héroe.
     * POST /heroes/create
     * Renarde maneja automáticamente el binding de parámetros del formulario.
     */
    @Transactional
    public void create(@BeanParam @Valid Hero hero) {
        if (validationFailed()) {
            render("form", hero, "create");
            return;
        }
        
        entityManager.persist(hero);
        entityManager.flush();
        
        flash("success", "Héroe creado exitosamente: " + hero.getName());
        redirect(Heroes.class).list();
    }
    
    /**
     * Mostrar formulario para editar héroe.
     * GET /heroes/edit/{id}
     * Template: templates/Heroes/form.html
     */
    public void edit(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero == null) {
            notFound();
            return;
        }
        render("form", hero, "update");
    }
    
    /**
     * Actualizar héroe existente.
     * POST /heroes/update/{id}
     */
    @Transactional
    public void update(Long id, @BeanParam @Valid Hero hero) {
        if (validationFailed()) {
            render("form", hero, "update");
            return;
        }
        
        Hero existingHero = entityManager.find(Hero.class, id);
        if (existingHero == null) {
            notFound();
            return;
        }
        
        existingHero.setName(hero.getName());
        existingHero.setPower(hero.getPower());
        existingHero.setPowerLevel(hero.getPowerLevel());
        if (hero.getDescription() != null) {
            existingHero.setDescription(hero.getDescription());
        }
        
        entityManager.merge(existingHero);
        
        flash("success", "Héroe actualizado exitosamente: " + existingHero.getName());
        redirect(Heroes.class).view(id);
    }
    
    /**
     * Eliminar héroe.
     * POST /heroes/delete/{id}
     */
    @Transactional
    public void delete(Long id) {
        Hero hero = entityManager.find(Hero.class, id);
        if (hero == null) {
            notFound();
            return;
        }
        
        String heroName = hero.getName();
        entityManager.remove(hero);
        
        flash("success", "Héroe eliminado: " + heroName);
        redirect(Heroes.class).list();
    }
}

