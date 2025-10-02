package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.RealmsBukkitPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RealmsPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Events.onChatMessage(
                new RealmsBukkitPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
