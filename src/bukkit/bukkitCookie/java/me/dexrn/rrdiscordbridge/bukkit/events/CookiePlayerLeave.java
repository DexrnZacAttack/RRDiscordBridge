package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CookiePlayerLeave extends PlayerListener {
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Events.onPlayerLeave(new CookieBukkitPlayer(event.getPlayer()));
    }
}
