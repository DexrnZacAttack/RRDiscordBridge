package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class CookiePlayerJoin extends PlayerListener {
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Events.onPlayerJoin(new CookieBukkitPlayer(event.getPlayer()));
    }
}
