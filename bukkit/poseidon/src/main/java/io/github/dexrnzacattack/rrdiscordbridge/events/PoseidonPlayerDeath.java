package io.github.dexrnzacattack.rrdiscordbridge.events;

import com.legacyminecraft.poseidon.event.PlayerDeathEvent;
import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PoseidonPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        // apparently this is recommended (by a PP dev) since PlayerDeathEvent is broken with no fix in sight
        // so enjoy this great bub fix
        if (event.getEntity() instanceof org.bukkit.entity.Player && event instanceof PlayerDeathEvent)
            Events.onPlayerDeath(new Player((org.bukkit.entity.Player) event.getEntity()), ((PlayerDeathEvent) event).getDeathMessage());
    }
}
