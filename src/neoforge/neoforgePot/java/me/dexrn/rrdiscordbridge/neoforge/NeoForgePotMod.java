package me.dexrn.rrdiscordbridge.neoforge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgePlayer;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeServer;

import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;

import org.apache.logging.log4j.LogManager;

public class NeoForgePotMod extends AbstractModernMinecraftMod {
    public NeoForgePotMod(Semver minecraftVer) {
        super(minecraftVer);
    }

    @Override
    public void init(MinecraftServer server) {
        NeoForge.EVENT_BUS.register(
                new NeoForgeEventHandler<>(NeoForgeServer::new, NeoForgePlayer::new));
        NeoForge.EVENT_BUS.register(
                new NeoForgePotEventHandler<>(NeoForgeServer::new, NeoForgePlayer::new));
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new NeoForgeServer(server),
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
