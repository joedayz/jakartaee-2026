package com.jakartaee.cdi.event;

import com.jakartaee.common.entities.Villain;

/**
 * Event que se dispara cuando se crea un nuevo villano.
 */
public class VillainCreatedEvent {
    
    private final Villain villain;
    private final long timestamp;
    
    public VillainCreatedEvent(Villain villain) {
        this.villain = villain;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Villain getVillain() {
        return villain;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "VillainCreatedEvent{" +
                "villain=" + villain.getName() +
                ", timestamp=" + timestamp +
                '}';
    }
}

