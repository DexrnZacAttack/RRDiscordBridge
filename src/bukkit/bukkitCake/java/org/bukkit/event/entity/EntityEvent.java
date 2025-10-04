package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Event;

public class EntityEvent extends Event {
    public EntityEvent(Event.Type t, Entity e) {
        super(t);
    }

    public final Entity getEntity() {
        return new Entity() { // fuck it
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }
}