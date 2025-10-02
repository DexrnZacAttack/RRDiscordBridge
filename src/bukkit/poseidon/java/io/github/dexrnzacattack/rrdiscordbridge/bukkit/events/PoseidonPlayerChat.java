package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.PoseidonPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PoseidonPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChatLegacy(PlayerChatEvent event) {
        Events.onChatMessage(
                new PoseidonPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
