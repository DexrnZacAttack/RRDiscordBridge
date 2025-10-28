package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.events.PoseidonPlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.PoseidonPlayerDeath;
import me.dexrn.rrdiscordbridge.bukkit.impls.PoseidonServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.plugin.java.JavaPlugin;

public class PoseidonPlugin extends BaseBukkitPlugin {
    public PoseidonPlugin(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new PoseidonServer(plugin.getServer()),
                        new JavaLogger(plugin.getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), plugin);
        pluginManager.registerEvents(new PoseidonPlayerChat(), plugin);
        pluginManager.registerEvents(new PoseidonPlayerDeath(), plugin);
    }
}
