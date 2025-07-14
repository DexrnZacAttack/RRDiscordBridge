package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerDeath;

public class OneDotOneBukkitPlugin extends BukkitPlugin {
    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new LegacyPlayerChat(), this);
        pluginManager.registerEvents(new LegacyPlayerDeath(), this);
    }
}
