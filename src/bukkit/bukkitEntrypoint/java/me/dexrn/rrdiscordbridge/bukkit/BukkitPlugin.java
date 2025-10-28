package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.multiversion.BukkitBasedPluginFactory;
import me.dexrn.rrdiscordbridge.multiversion.AbstractBukkitPlugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BukkitPlugin extends JavaPlugin {
    private AbstractBukkitPlugin plugin;

    public BukkitPlugin() {
        setup();
    }

    // for legacy bukkit
    public BukkitPlugin(
            PluginLoader pluginLoader,
            org.bukkit.Server instance,
            PluginDescriptionFile desc,
            File folder,
            File plug,
            ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plug, cLoader);
        setup();
    }

    private void setup() {
        BukkitBasedPluginFactory factory = new BukkitBasedPluginFactory();
        plugin = factory.getBukkitBasedPlugin(this).getInstance(this);

        if (plugin == null)
            throw new RuntimeException(
                    "This bukkit-based environment is not yet supported by RRDiscordBridge.");
    }

    @Override
    public void onEnable() {
        RRDiscordBridge.logger.info("Initializing %s", plugin.getClass().getName());
        ;
        plugin.init();
    }

    @Override
    public void onDisable() {
        if (RRDiscordBridge.instance != null) RRDiscordBridge.instance.shutdown(false);
    }
}
