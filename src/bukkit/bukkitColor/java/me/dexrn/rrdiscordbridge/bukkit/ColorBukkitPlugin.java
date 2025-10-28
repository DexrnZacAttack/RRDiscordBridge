package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerAdvancement;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerDeath;
import me.dexrn.rrdiscordbridge.bukkit.impls.RealmsBukkitServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.plugin.java.JavaPlugin;

public class ColorBukkitPlugin extends BaseBukkitPlugin {

    public ColorBukkitPlugin(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new RealmsBukkitServer(plugin.getServer()),
                        new JavaLogger(plugin.getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), plugin);
        pluginManager.registerEvents(new PlayerChat(), plugin);
        pluginManager.registerEvents(new PlayerDeath(), plugin);
        pluginManager.registerEvents(new PlayerAdvancement(), plugin);
    }
}
