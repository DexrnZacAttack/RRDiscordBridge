package me.dexrn.rrdiscordbridge.entrypoint;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.entrypoint.multiversion.BukkitBasedPluginFactory;
import me.dexrn.rrdiscordbridge.mc.multiversion.bukkit.AbstractBukkitPlugin;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BukkitEntrypoint extends JavaPlugin {
    private AbstractBukkitPlugin plugin;

    public BukkitEntrypoint() {
        setup();
    }

    // for legacy bukkit
    public BukkitEntrypoint(
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
        plugin.init();
    }

    @Override
    public void onDisable() {
        if (RRDiscordBridge.instance != null) RRDiscordBridge.instance.shutdown(false);
    }
}
