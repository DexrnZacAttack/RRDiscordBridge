package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerAdvancement;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.RealmsBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

public class ColorBukkitPlugin extends BukkitPlugin {

    @Override
    protected void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new RealmsBukkitServer(getServer()),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
        pluginManager.registerEvents(new PlayerAdvancement(), this);
    }
}
