package me.dexrn.rrdiscordbridge.mc.multiversion.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractBukkitPlugin implements IBukkitPlugin {
    protected final JavaPlugin plugin;

    public AbstractBukkitPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
