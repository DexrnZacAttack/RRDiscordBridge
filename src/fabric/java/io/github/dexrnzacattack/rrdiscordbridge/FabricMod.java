package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.impls.FabricServer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.util.logging.Logger;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // is this a good idea? Only way I can get access to a MinecraftServer instance, but also delays the mod loading until the server has loaded the world.
        ServerLifecycleEvents.SERVER_STARTED.register(
                server -> {
                    // ctor
                    RRDiscordBridge.instance =
                            new RRDiscordBridge(
                                    new FabricServer(server),
                                    Logger.getLogger("rrdiscordbridge")); // TODO: use slf4j

                    // then we init
                    RRDiscordBridge.instance.initialize();
                    RRDiscordBridge.instance.setSupportedFeatures(
                            new SupportedFeatures()
                                    .setCanGetServerMotd(true)
                                    .setCanGetServerName(false)
                                    .setCanQueryServerOperators(true));
                });
    }
}
