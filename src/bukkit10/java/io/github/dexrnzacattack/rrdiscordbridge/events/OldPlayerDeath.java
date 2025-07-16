package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class OldPlayerDeath extends EntityListener {
    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof CraftPlayer)
            Events.onPlayerDeath(new Player((org.bukkit.entity.Player) event.getEntity()), null);
    }
}
