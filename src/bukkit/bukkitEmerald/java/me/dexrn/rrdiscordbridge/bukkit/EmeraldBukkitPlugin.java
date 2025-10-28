package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.bukkit.events.PlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerDeath;

import org.bukkit.plugin.java.JavaPlugin;

public class EmeraldBukkitPlugin extends BaseBukkitPlugin {

    public EmeraldBukkitPlugin(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), plugin);
        pluginManager.registerEvents(new PlayerChat(), plugin);
        pluginManager.registerEvents(new PlayerDeath(), plugin);
    }
}
