package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitPlayer;

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
        Events.onPlayerCommand(new BukkitPlayer(event.getPlayer()), event.getMessage());
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        Events.onServerCommand(event.getCommand());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Events.onPlayerJoin(new BukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Events.onPlayerLeave(new BukkitPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Events.onPlayerKick(new BukkitPlayer(event.getPlayer()), event.getReason());
    }
}
