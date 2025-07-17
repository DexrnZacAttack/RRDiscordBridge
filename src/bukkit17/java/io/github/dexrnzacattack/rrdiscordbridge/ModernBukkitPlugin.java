package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerAchievement;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.impls.JavaLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.ModernBukkitServer;

public class ModernBukkitPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ModernBukkitServer(),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new PlayerChat(), this);
        pluginManager.registerEvents(new PlayerDeath(), this);
        pluginManager.registerEvents(new PlayerAchievement(), this);
    }
}
