package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.CakeBukkitPlayer;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class CakePlayerJoin extends PlayerListener {
    @Override
    public void onPlayerJoin(PlayerEvent event) {
        Events.onPlayerJoin(new CakeBukkitPlayer(event.getPlayer()));
    }
}
