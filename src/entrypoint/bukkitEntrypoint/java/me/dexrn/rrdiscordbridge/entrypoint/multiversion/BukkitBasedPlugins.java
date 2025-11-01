package me.dexrn.rrdiscordbridge.entrypoint.multiversion;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.mc.multiversion.bukkit.AbstractBukkitPlugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Function;

// ClassNotFoundException :broken_heart:
/** Defines the Plugin classes for each breaking Bukkit/MC/whatever version */
public enum BukkitBasedPlugins {
    /** Project Poseidon */
    POSEIDON(
            BukkitSupportCheck::supportsPoseidon, "me.dexrn.rrdiscordbridge.bukkit.PoseidonPlugin"),
    CAKE(BukkitSupportCheck::supportsCake, "me.dexrn.rrdiscordbridge.bukkit.CakeBukkitPlugin"),
    COOKIE(
            BukkitSupportCheck::supportsCookie,
            "me.dexrn.rrdiscordbridge.bukkit.CookieBukkitPlugin");

    private final String clazz;
    private final Function<JavaPlugin, Boolean> applicable;

    BukkitBasedPlugins(Function<JavaPlugin, Boolean> applicable, String cl) {
        this.applicable = applicable;
        this.clazz = cl;
    }

    public AbstractBukkitPlugin getInstance(JavaPlugin plugin) {
        try {
            return (AbstractBukkitPlugin)
                    Class.forName(clazz).getConstructor(JavaPlugin.class).newInstance(plugin);
        } catch (Exception e) {
            RRDiscordBridge.logger.error("Couldn't create Bukkit plugin class %s", clazz, e);
        }

        return null;
    }

    public boolean isSupported(JavaPlugin plugin) {
        return applicable.apply(plugin);
    }
}
