package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.LegacyPlayerChat;
import io.github.dexrnzacattack.rrdiscordbridge.events.PoseidonPlayerDeath;
import io.github.dexrnzacattack.rrdiscordbridge.impls.PoseidonServer;

import static io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper.doesMethodExist;

public class PoseidonPlugin extends BukkitPlugin {
    @Override
    public void setupBridge() {
        // ctor
        RRDiscordBridge.instance = new RRDiscordBridge(new PoseidonServer(), getServer().getLogger());

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
