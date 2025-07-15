package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OldPlayerLeave extends PlayerListener {
    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Events.onPlayerLeave(new Player(event.getPlayer()));
    }
}
