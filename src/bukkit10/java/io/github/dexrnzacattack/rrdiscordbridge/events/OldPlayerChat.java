package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.BukkitCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.BukkitPlayer;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerChat extends PlayerListener {
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Events.onChatMessage(
                new BukkitPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
