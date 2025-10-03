package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.*;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CookieBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class CookieBukkitPlugin extends BukkitPlugin {

    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new CookieBukkitServer(Bukkit.getServer()),
                        new JavaLogger(Bukkit.getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        // can't put them all in one file it seems
        pluginManager.registerEvent(
                Event.Type.PLAYER_CHAT, new CookiePlayerChat(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_COMMAND_PREPROCESS,
                new CookiePlayerCommand(),
                Event.Priority.High,
                this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_JOIN, new CookiePlayerJoin(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_KICK, new CookiePlayerKick(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_QUIT, new CookiePlayerLeave(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.ENTITY_DEATH, new CookiePlayerDeath(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.SERVER_COMMAND, new CookieServerCommand(), Event.Priority.High, this);
    }
}
