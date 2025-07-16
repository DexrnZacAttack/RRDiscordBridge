package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class BukkitEventHandler implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Events.onPlayerCommand(new Player(event.getPlayer()), event.getMessage());
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        Events.onServerCommand(event.getCommand());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Events.onPlayerJoin(new Player(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Events.onPlayerLeave(new Player(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Events.onPlayerKick(new Player(event.getPlayer()), event.getReason());
    }
}
