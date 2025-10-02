package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerDeath;

public class EmeraldBukkitPlugin extends BukkitPlugin {

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
    }
}
