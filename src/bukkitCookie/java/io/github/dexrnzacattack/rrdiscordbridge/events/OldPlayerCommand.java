package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.BukkitPlayer;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerCommand extends PlayerListener {
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Events.onPlayerCommand(new BukkitPlayer(event.getPlayer()), event.getMessage());
    }
}
