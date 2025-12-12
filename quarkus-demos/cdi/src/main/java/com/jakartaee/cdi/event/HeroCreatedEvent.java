package com.jakartaee.cdi.event;

import com.jakartaee.common.entities.Hero;

/**
 * Event que se dispara cuando se crea un nuevo héroe.
 * Demuestra el uso de eventos CDI para desacoplar componentes.
 */
public class HeroCreatedEvent {
    
    private final Hero hero;
    private final long timestamp;
    
    public HeroCreatedEvent(Hero hero) {
        this.hero = hero;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Hero getHero() {
        return hero;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "HeroCreatedEvent{" +
                "hero=" + hero.getName() +
                ", timestamp=" + timestamp +
                '}';
    }
}

