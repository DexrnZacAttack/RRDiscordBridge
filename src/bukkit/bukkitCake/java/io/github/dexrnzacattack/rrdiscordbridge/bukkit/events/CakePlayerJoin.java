package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CakeBukkitPlayer;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class CakePlayerJoin extends PlayerListener {
    @Override
    public void onPlayerJoin(PlayerEvent event) {
        Events.onPlayerJoin(new CakeBukkitPlayer(event.getPlayer()));
    }
}
