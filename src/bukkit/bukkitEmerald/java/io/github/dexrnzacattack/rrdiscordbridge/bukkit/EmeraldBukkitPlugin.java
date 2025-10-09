package io.github.dexrnzacattack.rrdiscordbridge.bukkit;

import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.events.PlayerDeath;

public class EmeraldBukkitPlugin extends BukkitPlugin {

    @Override
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
    }
}
