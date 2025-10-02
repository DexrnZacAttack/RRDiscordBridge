package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;

import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;

public class CookieServerCommand extends ServerListener {
    @Override
    public void onServerCommand(ServerCommandEvent event) {
        Events.onServerCommand(event.getCommand());
    }
}
