package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CookiePlayerLeave extends PlayerListener {
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Events.onPlayerLeave(new CookieBukkitPlayer(event.getPlayer()));
    }
}
