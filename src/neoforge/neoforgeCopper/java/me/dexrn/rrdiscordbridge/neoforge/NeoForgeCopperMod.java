package me.dexrn.rrdiscordbridge.neoforge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeCopperPlayer;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeCopperServer;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;

import org.apache.logging.log4j.LogManager;

public class NeoForgeCopperMod extends AbstractModernMinecraftMod {
    public NeoForgeCopperMod(Semver minecraftVer) {
        super(minecraftVer);
    }

    @Override
    public void init(MinecraftServer server) {
        NeoForge.EVENT_BUS.register(
                new NeoForgeEventHandler<>(NeoForgeCopperServer::new, NeoForgeCopperPlayer::new));
        NeoForge.EVENT_BUS.register(
                new NeoForgePotEventHandler<>(
                        NeoForgeCopperServer::new, NeoForgeCopperPlayer::new));
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new NeoForgeCopperServer(server),
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
