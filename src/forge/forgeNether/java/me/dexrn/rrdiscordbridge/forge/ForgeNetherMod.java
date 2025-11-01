package me.dexrn.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeNetherCommandCaller;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeNetherPlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeNetherServer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeNetherServerHandler;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftCommands;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import org.apache.logging.log4j.LogManager;

public class ForgeNetherMod extends AbstractModernMinecraftMod {
    public ForgeNetherMod(Semver minecraftVer) {
        super(minecraftVer);
        EVENT_BUS.register(this);
    }

    @Override
    public void init(MinecraftServer server) {
        EVENT_BUS.register(
                new ForgeCliffsEventHandler<>(ForgeNetherServer::new, ForgeNetherPlayer::new));

        (new ModernMinecraftCommands(ForgeNetherCommandCaller::new))
                .register(server.getCommands().getDispatcher());
    }

    @Override
    public void preInit() {}

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeNetherServer(server),
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
    public void onServerStarting(FMLServerAboutToStartEvent event) {
        ForgeNetherServerHandler.onServerStarting(event, this);
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        ForgeNetherServerHandler.onServerStopping(event);
    }
}
