package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.events.PoseidonPlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.PoseidonPlayerDeath;
import me.dexrn.rrdiscordbridge.bukkit.impls.PoseidonServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

public class PoseidonPlugin extends BukkitPlugin {
    @Override
    protected void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new PoseidonServer(getServer()),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PoseidonPlayerChat(), this);
        pluginManager.registerEvents(new PoseidonPlayerDeath(), this);
    }
}
