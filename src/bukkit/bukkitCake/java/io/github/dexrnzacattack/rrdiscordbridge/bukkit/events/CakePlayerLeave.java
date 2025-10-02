package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CakeBukkitPlayer;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class CakePlayerLeave extends PlayerListener {
    @Override
    public void onPlayerQuit(PlayerEvent event) {
        Events.onPlayerLeave(new CakeBukkitPlayer(event.getPlayer()));
    }
}
