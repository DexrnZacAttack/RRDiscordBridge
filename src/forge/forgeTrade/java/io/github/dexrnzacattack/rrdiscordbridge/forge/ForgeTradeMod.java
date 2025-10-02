package io.github.dexrnzacattack.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgeServer;
import io.github.dexrnzacattack.rrdiscordbridge.forge.multiversion.IForgeMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.SLF4JLogger;

import net.minecraft.server.MinecraftServer;

import org.slf4j.LoggerFactory;

public class ForgeTradeMod implements IForgeMod {
    public ForgeTradeMod() {
        EVENT_BUS.register(this);
    }

    @Override
    public void init(MinecraftServer server, Semver minecraftVersion) {
        EVENT_BUS.register(new ForgeEventHandler());
        EVENT_BUS.register(new ForgeTradeEventHandler());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeServer(server),
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
