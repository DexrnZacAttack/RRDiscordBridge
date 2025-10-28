package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.bukkit.events.LegacyPlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.LegacyPlayerDeath;

import org.bukkit.plugin.java.JavaPlugin;

public class FlatBukkitPlugin extends BaseBukkitPlugin {
    public FlatBukkitPlugin(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), plugin);
        pluginManager.registerEvents(new LegacyPlayerChat(), plugin);
        pluginManager.registerEvents(new LegacyPlayerDeath(), plugin);
    }
}
