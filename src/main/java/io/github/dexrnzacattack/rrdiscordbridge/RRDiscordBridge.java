package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.discord.AboutCommand;
import io.github.dexrnzacattack.rrdiscordbridge.eventcompatibility.legacy.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.eventcompatibility.legacy.LegacyPlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.eventcompatibility.modern.*;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.DiscordCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.FancyBroadcastCommand;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.ReloadConfigCommand;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class RRDiscordBridge extends JavaPlugin {
    public static Color REAL_ORANGE = new Color(255, 100, 0);
    public static Settings settings;
    public static String version;
    public static long serverStartTime;

    @Override
    public void onEnable() {
        serverStartTime = System.currentTimeMillis();
        version = getDescription().getVersion();
        try {
            settings = new Settings().loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            DiscordBot.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getCommand("reloadrdbconfig").setExecutor(new ReloadConfigCommand());
        getCommand("dcbroadcast").setExecutor(new FancyBroadcastCommand());
        getCommand("discord").setExecutor(new DiscordCommand());

        if (!AboutCommand.isMotdSupported())
            getServer().getLogger().warning("MOTD is not supported on this version. There will be no MOTD when /about is used.");

        if (!AboutCommand.isServerIconSupported())
            getServer().getLogger().warning("server-icon.png is not supported on this version. There will be no icon when /about is used.");

        if (!AboutCommand.isServerNameSupported())
            getServer().getLogger().warning("server.properties' server-name is not supported on this version. There will be no server name when /about is used.");

        getServer().getPluginManager().registerEvents(new RREventHandler(), this);

        if (PlayerChat.isSupported()) {
            getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        } else {
            getServer().getLogger().info("Registering legacy PlayerChat handler.");
            getServer().getPluginManager().registerEvents(new LegacyPlayerChat(),  this);
        }

        if (PlayerDeath.isSupported()) {
            getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        } else {
            getServer().getLogger().info("Registering legacy PlayerDeath handler.");
            getServer().getPluginManager().registerEvents(new LegacyPlayerDeath(), this);
        }

        getServer().getLogger().info(String.format("RRDiscordBridge v%s has started.", version));
        DiscordBot.sendEvent(Settings.Events.SERVER_START, new MessageEmbed.AuthorInfo(null, null, null, null), null, Color.GREEN, "Server started!");
    }

    /** Backwards compatible getOnlinePlayers since 1.8 changed the type to Collection */
    @SuppressWarnings({"ConstantConditions", "SuspiciousToArrayCall"})
    public static Player[] getOnlinePlayers() {
        try {
            Method getOnlinePlayers = Bukkit.class.getMethod("getOnlinePlayers");
            Object players = getOnlinePlayers.invoke(null);

            if (players instanceof Player[]) {
                return (Player[]) players;
            } else if (players instanceof Collection) {
                Collection<?> collection = (Collection<?>) players;
                return collection.toArray(new Player[0]);
            }
        } catch (Exception ignored) {}
        return new Player[0];
    }


    @Override
    public void onDisable() {
        CompletableFuture<Void> eventFuture = CompletableFuture.runAsync(() -> {
            DiscordBot.sendEvent(Settings.Events.SERVER_STOP, new MessageEmbed.AuthorInfo(null, null, null, null), null, Color.RED, "Server stopped!");
        });

        eventFuture.thenRun(DiscordBot::stop);
    }
}