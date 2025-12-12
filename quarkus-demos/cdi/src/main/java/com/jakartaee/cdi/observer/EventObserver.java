package com.jakartaee.cdi.observer;

import com.jakartaee.cdi.event.HeroCreatedEvent;
import com.jakartaee.cdi.event.VillainCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Observer que escucha eventos CDI.
 * Demuestra cómo usar @Observes para reaccionar a eventos disparados por otros componentes.
 */
@ApplicationScoped
public class EventObserver {
    
    private static final Logger logger = Logger.getLogger(EventObserver.class.getName());
    
    private final List<HeroCreatedEvent> heroEvents = new ArrayList<>();
    private final List<VillainCreatedEvent> villainEvents = new ArrayList<>();
    
    /**
     * Observer para eventos de creación de héroes.
     * Se ejecuta automáticamente cuando se dispara un HeroCreatedEvent.
     */
    public void onHeroCreated(@Observes HeroCreatedEvent event) {
        logger.info(String.format(">>> Observer: Héroe creado - %s (Power Level: %d)", 
                event.getHero().getName(), event.getHero().getPowerLevel()));
        heroEvents.add(event);
    }
    
    /**
     * Observer para eventos de creación de villanos.
     */
    public void onVillainCreated(@Observes VillainCreatedEvent event) {
        logger.warning(String.format(">>> Observer: Villano creado - %s (Power Level: %d, Threat: %s)", 
                event.getVillain().getName(), 
                event.getVillain().getPowerLevel(),
                event.getVillain().getThreatLevel()));
        villainEvents.add(event);
    }
    
    /**
     * Obtiene todos los eventos de héroes registrados.
     */
    public List<HeroCreatedEvent> getHeroEvents() {
        return new ArrayList<>(heroEvents);
    }
    
    /**
     * Obtiene todos los eventos de villanos registrados.
     */
    public List<VillainCreatedEvent> getVillainEvents() {
        return new ArrayList<>(villainEvents);
    }
    
    /**
     * Obtiene el conteo de eventos.
     */
    public int getHeroEventCount() {
        return heroEvents.size();
    }
    
    public int getVillainEventCount() {
        return villainEvents.size();
    }
}

