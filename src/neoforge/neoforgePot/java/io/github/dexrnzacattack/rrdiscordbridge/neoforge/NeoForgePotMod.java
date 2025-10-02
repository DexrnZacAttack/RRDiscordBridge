package io.github.dexrnzacattack.rrdiscordbridge.neoforge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.SLF4JLogger;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.impls.NeoForgeServer;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.multiversion.INeoForgeMod;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;

import org.slf4j.LoggerFactory;

public class NeoForgePotMod implements INeoForgeMod {
    @Override
    public void init(MinecraftServer server, Semver minecraftVersion) {
        NeoForge.EVENT_BUS.register(new NeoForgeEventHandler());
        NeoForge.EVENT_BUS.register(new NeoForgePotEventHandler());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new NeoForgeServer(server),
                        new SLF4JLogger(LoggerFactory.getLogger("RRDiscordBridge")),
                        ConfigDirectory.MOD);

        // then we init
        RRDiscordBridge.instance.initialize();
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(true)
                        .setCanGetServerName(false)
                        .setCanQueryServerOperators(true)
                        .setCanQueryPlayerHasJoinedBefore(false)
                        .setCanSendConsoleCommands(true));
    }
}
