package org.bukkit.event.entity;

import org.bukkit.Entity;
import org.bukkit.event.Cancellable;

public class EntityDamageEvent extends org.bukkit.event.entity.EntityEvent implements Cancellable {
    public EntityDamageEvent(Type t, Entity e) {
        super(t, e);
    }

    public int getDamage() {
        return 0;
    }

    public DamageCause getCause() {
        return DamageCause.STUB;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {}

    public static enum DamageCause {
        STUB
    }
}
