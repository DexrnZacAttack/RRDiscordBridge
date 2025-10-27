package me.dexrn.rrdiscordbridge.forge;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCopperPlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCopperServer;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.impls.vanilla.CommandCaller;
import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import me.dexrn.rrdiscordbridge.multiversion.AbstractModernMinecraftMod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import org.apache.logging.log4j.LogManager;

import java.lang.invoke.MethodHandles;

public class ForgeCopperMod extends AbstractModernMinecraftMod {
    public ForgeCopperMod(Semver ver) {
        super(ver);
        BusGroup.DEFAULT.register(MethodHandles.lookup(), this);
    }

    @Override
    public void init(MinecraftServer server) {
        BusGroup.DEFAULT.register(
                MethodHandles.lookup(),
                new ForgeSkiesEventHandler<>(ForgeCopperServer::new, ForgeCopperPlayer::new));

        (new ModernMinecraftCommands<>(CommandCaller::new))
                .register(server.getCommands().getDispatcher());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeCopperServer(server),
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
