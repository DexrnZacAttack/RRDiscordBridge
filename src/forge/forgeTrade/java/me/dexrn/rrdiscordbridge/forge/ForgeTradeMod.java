package me.dexrn.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeServer;
import me.dexrn.rrdiscordbridge.forge.multiversion.IForgeMod;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;

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
