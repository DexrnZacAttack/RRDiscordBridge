package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.commands.DiscordCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.commands.ExtensionsCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.commands.FancyBroadcastCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.commands.ReloadConfigCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {
    public static PluginManager pluginManager;

    // so it can be overridden
    public void setupBridge() {
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
