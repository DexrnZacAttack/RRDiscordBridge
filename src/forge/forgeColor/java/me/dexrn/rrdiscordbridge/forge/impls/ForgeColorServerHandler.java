package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraft.server.MinecraftServer;

public class ForgeColorServerHandler {
    public static void onServerStarting(MinecraftServer server, AbstractModernMinecraftMod mod) {
        mod.setupBridge(server);
        RRDiscordBridge.logger.info("Initializing %s", mod.getClass().getName());
        mod.init(server);
    }
}
