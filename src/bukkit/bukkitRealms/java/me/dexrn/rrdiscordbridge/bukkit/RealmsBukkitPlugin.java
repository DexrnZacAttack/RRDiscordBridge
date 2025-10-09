package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerAchievement;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerDeath;
import me.dexrn.rrdiscordbridge.bukkit.events.RealmsPlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.impls.RealmsBukkitServer;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.JavaLogger;

import org.bukkit.Bukkit;

public class RealmsBukkitPlugin extends BukkitPlugin {
    @Override
    protected void setupBridge() {
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
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new RealmsPlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
        pluginManager.registerEvents(new PlayerAchievement(), this);
    }
}
