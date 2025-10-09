package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.CakePlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.CakePlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.CakePlayerJoin;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.CakePlayerLeave;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.CakeBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class CakeBukkitPlugin extends JavaPlugin {
    public static org.bukkit.Server server;
    public static PluginManager pluginManager;

    public CakeBukkitPlugin(
            PluginLoader pluginLoader,
            org.bukkit.Server instance,
            PluginDescriptionFile desc,
            File folder,
            File plugin,
            ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        server = instance;
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

    protected void registerEvents() {
        // can't put them all in one file it seems
        pluginManager.registerEvent(
                Event.Type.PLAYER_CHAT, new CakePlayerChat(), Event.Priority.High, this);
        //        pluginManager.registerEvent(
        //                Event.Type.PLAYER_COMMAND_PREPROCESS,
        //                new OldPlayerCommand(),
        //                Event.Priority.High,
        //                this);
        pluginManager.registerEvent(
                Event.Type.PLAYER_JOIN, new CakePlayerJoin(), Event.Priority.High, this);
//        pluginManager.registerEvent(
//                Event.Type.ENTITY_DAMAGED, new CakePlayerDeath(), Event.Priority.High, this);
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
                Event.Type.PLAYER_QUIT, new CakePlayerLeave(), Event.Priority.High, this);
//        pluginManager.registerEvent(
//                Event.Type.ENTITY_DEATH, new CakePlayerDeath(), Event.Priority.High, this);
        //        pluginManager.registerEvent(
        //                Event.Type.SERVER_COMMAND, new OldServerCommand(), Event.Priority.High,
        // this);
    }

    protected void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new CakeBukkitServer(getServer()),
                        new JavaLogger(Logger.getLogger("RRDiscordBridge")),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void onEnable() {
        setupBridge();

        // plugin manager
        pluginManager = getServer().getPluginManager();

        registerEvents();
    }

    @Override
    public void onDisable() {
        if (RRDiscordBridge.instance != null) RRDiscordBridge.instance.shutdown(false);
    }
}
