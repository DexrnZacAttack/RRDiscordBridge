package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerCommand extends PlayerListener {
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Events.onPlayerCommand(new Player(event.getPlayer()), event.getMessage());
    }
}
