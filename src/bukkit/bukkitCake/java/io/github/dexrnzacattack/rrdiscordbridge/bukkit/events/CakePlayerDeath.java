package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CakeBukkitPlayer;

import org.bukkit.Player;
import org.bukkit.Entity;
import org.bukkit.craftbukkit.CraftPlayer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

// unused
public class CakePlayerDeath extends EntityListener {
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof CraftPlayer) {
            Player p = (org.bukkit.Player) event.getEntity();
            if ((p.getHealth() - event.getDamage()) < 1) { // assume player has died
                Events.onPlayerDeath(
                        new CakeBukkitPlayer(p),
                        String.format("%s was slain by %s", p.getName(), event.getCause().name()));
            }
        }
    }

    @Override
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        onEntityDamage(event);
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        onEntityDamage(event);
    }
}
