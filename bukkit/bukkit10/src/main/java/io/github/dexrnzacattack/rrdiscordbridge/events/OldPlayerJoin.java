package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerJoin extends PlayerListener {
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Events.onPlayerJoin(new Player(event.getPlayer()));
    }
}
