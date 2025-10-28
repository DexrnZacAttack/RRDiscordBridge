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
import me.dexrn.rrdiscordbridge.multiversion.AbstractBukkitPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BaseBukkitPlugin extends AbstractBukkitPlugin {
    public static PluginManager pluginManager;

    public BaseBukkitPlugin(JavaPlugin plugin) {
        super(plugin);
    }

    // so it can be overridden
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new BukkitServer(Bukkit.getServer()),
                        new JavaLogger(plugin.getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    public void setSupportedFeatures() {
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

    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), plugin);
    }

    @Override
    public void init() {
        setupBridge();

        // plugin manager
        pluginManager = plugin.getServer().getPluginManager();

        // register all the in-game commands
        // cernel extension
        plugin.getCommand(CommandRegistry.CommandName.RDBEXT.getName())
                .setExecutor(new ExtensionsCommand());
        plugin.getCommand(CommandRegistry.CommandName.DCBROADCAST.getName())
                .setExecutor(new FancyBroadcastCommand());
        plugin.getCommand(CommandRegistry.CommandName.DISCORD.getName())
                .setExecutor(new DiscordCommand());
        plugin.getCommand(CommandRegistry.CommandName.RELOADCONFIG.getName())
                .setExecutor(new ReloadConfigCommand());

        registerEvents();
    }
}
