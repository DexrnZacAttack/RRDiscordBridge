package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.bukkit.events.LegacyPlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.LegacyPlayerDeath;

public class FlatBukkitPlugin extends BukkitPlugin {
    @Override
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new LegacyPlayerChat(), this);
        pluginManager.registerEvents(new LegacyPlayerDeath(), this);
    }
}
