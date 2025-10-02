package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitPlayer;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class CookiePlayerCommand extends PlayerListener {
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Events.onPlayerCommand(new CookieBukkitPlayer(event.getPlayer()), event.getMessage());
    }
}
