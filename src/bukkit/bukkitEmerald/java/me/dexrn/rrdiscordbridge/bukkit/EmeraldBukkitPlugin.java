package me.dexrn.rrdiscordbridge.bukkit;

import me.dexrn.rrdiscordbridge.bukkit.events.PlayerChat;
import me.dexrn.rrdiscordbridge.bukkit.events.PlayerDeath;

public class EmeraldBukkitPlugin extends BukkitPlugin {

    @Override
    protected void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
    }
}
