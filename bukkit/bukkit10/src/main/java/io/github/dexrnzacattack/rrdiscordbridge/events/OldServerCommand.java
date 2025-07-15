package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;

public class OldServerCommand extends ServerListener {
    @Override
    public void onServerCommand(ServerCommandEvent event) {
        Events.onServerCommand(event.getCommand());
    }
}
