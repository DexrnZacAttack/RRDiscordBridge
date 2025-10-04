package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Cancellable;

public class EntityDamageByBlockEvent extends EntityDamageEvent implements Cancellable {
    public EntityDamageByBlockEvent(Type t, Entity e) {
        super(t, e);
    }
}
