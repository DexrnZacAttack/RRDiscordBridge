package me.dexrn.rrdiscordbridge.bukkit;

import static me.dexrn.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.bukkit.commands.DiscordCommand;
import me.dexrn.rrdiscordbridge.bukkit.commands.ExtensionsCommand;
import me.dexrn.rrdiscordbridge.bukkit.commands.FancyBroadcastCommand;
import me.dexrn.rrdiscordbridge.bukkit.commands.ReloadConfigCommand;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitServer;
import me.dexrn.rrdiscordbridge.command.CommandRegistry;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    public static PluginManager pluginManager;

    // so it can be overridden
    protected void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new BukkitServer(Bukkit.getServer()),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    protected void setSupportedFeatures() {
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(doesMethodExist("org.bukkit.Server", "getMotd"))
                        .setCanGetServerName(doesMethodExist("org.bukkit.Server", "getServerName"))
                        .setCanQueryServerOperators(
                                doesMethodExist("org.bukkit.Server", "getOperators"))
                        .setCanQueryPlayerHasJoinedBefore(
                                doesMethodExist("org.bukkit.entity.Player", "hasPlayedBefore"))
                        .setCanSendConsoleCommands(
                                doesMethodExist("org.bukkit.Server", "getConsoleSender")));
    }

    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
    }

    @Override
    public void onEnable() {
        setupBridge();

        // plugin manager
        pluginManager = getServer().getPluginManager();

        // register all the in-game commands
        // cernel extension
        getCommand(CommandRegistry.CommandName.RDBEXT.getName())
                .setExecutor(new ExtensionsCommand());
        getCommand(CommandRegistry.CommandName.DCBROADCAST.getName())
                .setExecutor(new FancyBroadcastCommand());
        getCommand(CommandRegistry.CommandName.DISCORD.getName()).setExecutor(new DiscordCommand());
        getCommand(CommandRegistry.CommandName.RELOADCONFIG.getName())
                .setExecutor(new ReloadConfigCommand());

        registerEvents();
    }

    @Override
    public void onDisable() {
        if (RRDiscordBridge.instance != null) RRDiscordBridge.instance.shutdown(false);
    }
}
