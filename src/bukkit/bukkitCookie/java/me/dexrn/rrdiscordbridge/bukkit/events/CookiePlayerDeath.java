package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class CookiePlayerDeath extends EntityListener {
    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof CraftPlayer)
            Events.onPlayerDeath(
                    new CookieBukkitPlayer((org.bukkit.entity.Player) event.getEntity()), null);
    }
}
