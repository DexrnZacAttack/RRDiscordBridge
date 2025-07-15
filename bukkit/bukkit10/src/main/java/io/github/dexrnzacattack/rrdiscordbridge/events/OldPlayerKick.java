package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerKick extends PlayerListener {
    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        Events.onPlayerKick(new Player(event.getPlayer()), event.getReason());
    }
}
