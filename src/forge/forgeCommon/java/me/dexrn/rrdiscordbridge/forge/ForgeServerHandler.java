package me.dexrn.rrdiscordbridge.forge;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

public class ForgeServerHandler {
    public static void onServerStarting(
            ServerAboutToStartEvent event, AbstractModernMinecraftMod mod) {
        mod.setupBridge(event.getServer());
        RRDiscordBridge.logger.info("Initializing %s", mod.getClass().getName());
        mod.init(event.getServer());
    }

    public static void onServerStopping(ServerStoppingEvent event) {
        RRDiscordBridge.instance.shutdown(false);
    }
}
