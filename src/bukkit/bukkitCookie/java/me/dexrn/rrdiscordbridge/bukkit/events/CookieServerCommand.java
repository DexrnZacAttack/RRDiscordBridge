package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;

import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;

public class CookieServerCommand extends ServerListener {
    @Override
    public void onServerCommand(ServerCommandEvent event) {
        Events.onServerCommand(event.getCommand());
    }
}
