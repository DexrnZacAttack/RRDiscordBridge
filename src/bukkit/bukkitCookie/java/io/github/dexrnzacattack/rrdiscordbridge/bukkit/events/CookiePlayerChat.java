package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class CookiePlayerChat extends PlayerListener {
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Events.onChatMessage(
                new CookieBukkitPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
