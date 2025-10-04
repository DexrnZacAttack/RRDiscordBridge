package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Cancellable;

public class EntityDamageByEntityEvent extends EntityDamageEvent implements Cancellable {
    public EntityDamageByEntityEvent(Type t, Entity e) {
        super(t, e);
    }
}
