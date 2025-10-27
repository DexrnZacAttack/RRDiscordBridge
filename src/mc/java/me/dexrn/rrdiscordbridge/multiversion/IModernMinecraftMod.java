package me.dexrn.rrdiscordbridge.multiversion;

import net.minecraft.server.MinecraftServer;

public interface IModernMinecraftMod {
    void init(MinecraftServer server);

    void preInit();

    void setupBridge(MinecraftServer server);
}
