package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.*;
import io.github.dexrnzacattack.rrdiscordbridge.impls.CookieBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.impls.JavaLogger;

import org.bukkit.event.Event;

public class CookieBukkitPlugin extends EmeraldBukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new CookieBukkitServer(),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        // can't put them all in one file it seems
        pluginManager.registerEvent(
                Event.Type.PLAYER_CHAT, new OldPlayerChat(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_COMMAND_PREPROCESS,
                new OldPlayerCommand(),
                Event.Priority.High,
                this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_JOIN, new OldPlayerJoin(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_KICK, new OldPlayerKick(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_QUIT, new OldPlayerLeave(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.ENTITY_DEATH, new OldPlayerDeath(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.SERVER_COMMAND, new OldServerCommand(), Event.Priority.High, this);
    }
}
