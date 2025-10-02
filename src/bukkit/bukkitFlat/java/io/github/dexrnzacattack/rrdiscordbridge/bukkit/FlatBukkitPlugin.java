package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.LegacyPlayerDeath;

public class FlatBukkitPlugin extends BukkitPlugin {
    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new LegacyPlayerChat(), this);
        pluginManager.registerEvents(new LegacyPlayerDeath(), this);
    }
}
