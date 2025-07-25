package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import net.minecraft.server.MinecraftServer;

public interface IFabricMod {
    void init(MinecraftServer server, Semver minecraftVersion);

    void preInit();

    void setupBridge(MinecraftServer server);
}
