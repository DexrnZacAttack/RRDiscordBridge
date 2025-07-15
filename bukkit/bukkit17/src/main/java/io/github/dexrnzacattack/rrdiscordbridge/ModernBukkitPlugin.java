package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.impls.ModernServer;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

public class ModernBukkitPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance = new RRDiscordBridge(new ModernServer(), getServer().getLogger());

        // then we init
        RRDiscordBridge.instance.initialize();
        setSupportedFeatures();
    }
}
