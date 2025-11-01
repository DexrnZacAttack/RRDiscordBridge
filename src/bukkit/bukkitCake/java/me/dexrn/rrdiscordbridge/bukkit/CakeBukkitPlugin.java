package me.dexrn.rrdiscordbridge.bukkit;

import static me.dexrn.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.bukkit.events.CakePlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.CakePlayerJoin;
import me.dexrn.rrdiscordbridge.bukkit.events.CakePlayerLeave;
import me.dexrn.rrdiscordbridge.bukkit.impls.CakeBukkitServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;
import me.dexrn.rrdiscordbridge.mc.multiversion.bukkit.AbstractBukkitPlugin;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CakeBukkitPlugin extends AbstractBukkitPlugin {
    public static org.bukkit.Server server;
    public static PluginManager pluginManager;

    public CakeBukkitPlugin(JavaPlugin plugin) {
        super(plugin);
        server = plugin.getServer();
    }

    public void setSupportedFeatures() {
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(doesMethodExist("org.bukkit.Server", "getMotd"))
                        .setCanGetServerName(doesMethodExist("org.bukkit.Server", "getServerName"))
                        .setCanQueryServerOperators(
                                doesMethodExist("org.bukkit.Server", "getOperators"))
                        .setCanQueryPlayerHasJoinedBefore(
                                doesMethodExist("org.bukkit.Player", "hasPlayedBefore"))
                        .setCanSendConsoleCommands(
                                doesMethodExist("org.bukkit.Server", "getConsoleSender")));
    }

    public void registerEvents() {
        // can't put them all in one file it seems
        pluginManager.registerEvent(
                Event.Type.PLAYER_CHAT, new CakePlayerChat(), Event.Priority.High, plugin);
        //        pluginManager.registerEvent(
        //                Event.Type.PLAYER_COMMAND_PREPROCESS,
        //                new OldPlayerCommand(),
        //                Event.Priority.High,
        //                this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_JOIN, new CakePlayerJoin(), Event.Priority.High, plugin);
        //        pluginManager.registerEvent(
        //                Event.Type.ENTITY_DAMAGED, new CakePlayerDeath(), Event.Priority.High,
        // this);
        //        pluginManager.registerEvent(
        //                Event.Type.ENTITY_DAMAGEDBY_BLOCK,
        //                new CakePlayerDeath(),
        //                Event.Priority.High,
        //                this);
        //        pluginManager.registerEvent(
        //                Event.Type.ENTITY_DAMAGEDBY_ENTITY,
        //                new CakePlayerDeath(),
        //                Event.Priority.High,
        //                this);
        //        pluginManager.registerEvent(
        //                Event.Type.PLAYER_KICK, new OldPlayerKick(), Event.Priority.High, this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_QUIT, new CakePlayerLeave(), Event.Priority.High, plugin);
        //        pluginManager.registerEvent(
        //                Event.Type.ENTITY_DEATH, new CakePlayerDeath(), Event.Priority.High,
        // this);
        //        pluginManager.registerEvent(
        //                Event.Type.SERVER_COMMAND, new OldServerCommand(), Event.Priority.High,
        // this);
    }

    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new CakeBukkitServer(plugin.getServer()),
                        new JavaLogger(Logger.getLogger("RRDiscordBridge")),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void init() {
        setupBridge();

        // plugin manager
        pluginManager = plugin.getServer().getPluginManager();

        registerEvents();
    }
}
