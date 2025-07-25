package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerDeath;

public class FlatBukkitPlugin extends EmeraldBukkitPlugin {
    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new LegacyPlayerChat(), this);
        pluginManager.registerEvents(new LegacyPlayerDeath(), this);
    }
}
