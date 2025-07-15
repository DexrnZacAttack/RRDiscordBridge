package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class OldPlayerChat extends PlayerListener {
    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Events.onChatMessage(new Player(event.getPlayer()), event.getMessage(), new Cancellable(event));
    }
}
