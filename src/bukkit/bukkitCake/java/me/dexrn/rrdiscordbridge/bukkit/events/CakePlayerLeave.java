package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.CakeBukkitPlayer;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class CakePlayerLeave extends PlayerListener {
    @Override
    public void onPlayerQuit(PlayerEvent event) {
        Events.onPlayerLeave(new CakeBukkitPlayer(event.getPlayer()));
    }
}
