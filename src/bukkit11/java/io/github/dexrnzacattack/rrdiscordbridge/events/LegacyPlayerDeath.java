package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LegacyPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeathLegacy(PlayerDeathEvent event) {
        Events.onPlayerDeath(null, event.getDeathMessage());
    }
}
