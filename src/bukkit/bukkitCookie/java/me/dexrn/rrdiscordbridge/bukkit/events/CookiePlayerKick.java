package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

public class CookiePlayerKick extends PlayerListener {
    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        Events.onPlayerKick(new CookieBukkitPlayer(event.getPlayer()), event.getReason());
    }
}
