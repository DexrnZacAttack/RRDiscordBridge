package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.*;
import io.github.dexrnzacattack.rrdiscordbridge.impls.OldBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

public class OldBukkitPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance = new RRDiscordBridge(new OldBukkitServer(), getServer().getLogger());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        // can't put them all in one file it seems
        pluginManager.registerEvent(Event.Type.PLAYER_CHAT, new OldPlayerChat(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, new OldPlayerCommand(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.PLAYER_JOIN, new OldPlayerJoin(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.PLAYER_KICK, new OldPlayerKick(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.PLAYER_QUIT, new OldPlayerLeave(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.ENTITY_DEATH, new OldPlayerDeath(), Event.Priority.High, this);
        pluginManager.registerEvent(Event.Type.SERVER_COMMAND, new OldServerCommand(), Event.Priority.High, this);
    }
}
