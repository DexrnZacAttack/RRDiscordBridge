package me.dexrn.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCliffsCommandCaller;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCliffsPlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCliffsServer;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftCommands;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import org.apache.logging.log4j.LogManager;

public class ForgeCliffsMod extends AbstractModernMinecraftMod {
    public ForgeCliffsMod(Semver minecraftVer) {
        super(minecraftVer);
        EVENT_BUS.register(this);
    }

    @Override
    public void init(MinecraftServer server) {
        EVENT_BUS.register(
                new ForgeCliffsEventHandler<>(ForgeCliffsServer::new, ForgeCliffsPlayer::new));

        (new ModernMinecraftCommands(ForgeCliffsCommandCaller::new))
                .register(server.getCommands().getDispatcher());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeCliffsServer(server),
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

    @SubscribeEvent
    public void onServerStarting(ServerAboutToStartEvent event) {
        ForgeServerHandler.onServerStarting(event, this);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        ForgeServerHandler.onServerStopping(event);
    }
}
