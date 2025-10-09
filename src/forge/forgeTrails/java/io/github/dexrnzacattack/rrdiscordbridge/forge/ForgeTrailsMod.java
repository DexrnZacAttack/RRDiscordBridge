package io.github.dexrnzacattack.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgeTrailsServer;
import io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion.IForgeMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.Log4JLogger;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;

public class ForgeTrailsMod implements IForgeMod {
    public ForgeTrailsMod() {
        EVENT_BUS.register(this);
    }

    @Override
    public void init(MinecraftServer server, Semver minecraftVersion) {
        EVENT_BUS.register(new ForgeEventHandler());
        EVENT_BUS.register(new ForgeTrailsEventHandler());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeTrailsServer(server),
                        new Log4JLogger(LogManager.getLogger("RRDiscordBridge")),
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
