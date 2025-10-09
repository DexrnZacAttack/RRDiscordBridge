package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.events.*;
import me.dexrn.rrdiscordbridge.bukkit.impls.CookieBukkitServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class CookieBukkitPlugin extends BukkitPlugin {
    @Override
    protected void setupBridge() {
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
    protected void registerEvents() {
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
