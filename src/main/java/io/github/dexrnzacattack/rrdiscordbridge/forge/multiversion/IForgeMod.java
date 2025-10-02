package io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion;

import com.vdurmont.semver4j.Semver;

import net.minecraft.server.MinecraftServer;

public interface IForgeMod {
    void init(MinecraftServer server, Semver minecraftVersion);

    void preInit();

    void setupBridge(MinecraftServer server);
}
