package io.github.dexrnzacattack.rrdiscordbridge;

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
                        bukkitConfigPath);

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }
}
