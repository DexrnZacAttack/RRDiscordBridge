package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerAdvancement;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.RealmsPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.RealmsBukkitServer;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;

public class VexBukkitPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new RealmsBukkitServer(Bukkit.getServer()),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new RealmsPlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
        pluginManager.registerEvents(new PlayerAdvancement(), this);
    }
}
