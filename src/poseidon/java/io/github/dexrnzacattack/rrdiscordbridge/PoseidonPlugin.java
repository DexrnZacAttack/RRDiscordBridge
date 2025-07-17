package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.PoseidonPlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.impls.JavaLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.PoseidonServer;

public class PoseidonPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new PoseidonServer(),
                        new JavaLogger(getServer().getLogger()),
                        ConfigDirectory.PLUGIN.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }

    @Override
    public void registerEvents() {
        pluginManager.registerEvents(new BukkitEventHandler(), this);
        pluginManager.registerEvents(new LegacyPlayerChat(), this);
        pluginManager.registerEvents(new PoseidonPlayerDeath(), this);
    }
}
