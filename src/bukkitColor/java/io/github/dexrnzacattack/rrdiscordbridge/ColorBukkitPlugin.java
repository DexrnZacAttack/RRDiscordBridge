package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerAdvancement;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.impls.JavaLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.RealmsBukkitServer;

public class ColorBukkitPlugin extends EmeraldBukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new RealmsBukkitServer(),
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
        pluginManager.registerEvents(new PlayerAdvancement(), this);
    }
}
