package io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion;

import com.vdurmont.semver4j.Semver;

import net.minecraft.server.MinecraftServer;

public interface INeoForgeMod {
    void init(MinecraftServer server, Semver minecraftVersion);

    void preInit();

    void setupBridge(MinecraftServer server);
}
