package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class LegacyPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChatLegacy(PlayerChatEvent event) {
        Events.onChatMessage(new Player(event.getPlayer()), event.getMessage(), new Cancellable(event));
    }
}
