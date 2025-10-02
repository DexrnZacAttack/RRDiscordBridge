package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LegacyPlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeathLegacy(PlayerDeathEvent event) {
        Events.onPlayerDeath(new BukkitPlayer((Player) event.getEntity()), event.getDeathMessage());
    }
}
