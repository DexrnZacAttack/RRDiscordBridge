package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PoseidonPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PoseidonPlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.PoseidonServer;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

public class PoseidonPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
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
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PoseidonPlayerChat(), this);
        pluginManager.registerEvents(new PoseidonPlayerDeath(), this);
    }
}
