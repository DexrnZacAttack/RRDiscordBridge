package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

public class CookiePlayerKick extends PlayerListener {
    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        Events.onPlayerKick(new CookieBukkitPlayer(event.getPlayer()), event.getReason());
    }
}
