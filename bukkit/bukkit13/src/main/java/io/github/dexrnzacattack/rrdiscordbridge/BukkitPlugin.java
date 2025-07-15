package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.commands.ChatExtensionsCommand;
import io.github.dexrnzacattack.rrdiscordbridge.commands.DiscordCommand;
import io.github.dexrnzacattack.rrdiscordbridge.commands.FancyBroadcastCommand;
import io.github.dexrnzacattack.rrdiscordbridge.commands.ReloadConfigCommand;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Server;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

public class BukkitPlugin extends JavaPlugin {
    public static PluginManager pluginManager;

    // so it can be overridden
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance = new RRDiscordBridge(new Server(), getServer().getLogger());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    public void setSupportedFeatures() {
        RRDiscordBridge.instance.setSupportedFeatures(new SupportedFeatures()
                .setCanGetServerMotd(doesMethodExist("org.bukkit.Server", "getMotd"))
                .setCanGetServerName(doesMethodExist("org.bukkit.Server", "getServerName"))
                .setCanQueryServerOperators(doesMethodExist("org.bukkit.Server", "getOperators"))
        );
    }

    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
    }

    @Override
    public void onEnable() {
        setupBridge();

        // plugin manager
        pluginManager = getServer().getPluginManager();

        // register all the in-game commands
        // cernel extension
        getCommand(CommandRegistry.CommandName.CEXT.getName()).setExecutor(new ChatExtensionsCommand());
        getCommand(CommandRegistry.CommandName.DCBROADCAST.getName()).setExecutor(new FancyBroadcastCommand());
        getCommand(CommandRegistry.CommandName.DISCORD.getName()).setExecutor(new DiscordCommand());
        getCommand(CommandRegistry.CommandName.RELOADCONFIG.getName()).setExecutor(new ReloadConfigCommand());

        registerEvents();
    }

    @Override
    public void onDisable() {
        if (RRDiscordBridge.instance != null)
            RRDiscordBridge.instance.shutdown();
    }
}