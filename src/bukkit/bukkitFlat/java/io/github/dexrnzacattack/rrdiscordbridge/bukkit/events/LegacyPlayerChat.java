package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class LegacyPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChatLegacy(PlayerChatEvent event) {
        Events.onChatMessage(
                new BukkitPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
