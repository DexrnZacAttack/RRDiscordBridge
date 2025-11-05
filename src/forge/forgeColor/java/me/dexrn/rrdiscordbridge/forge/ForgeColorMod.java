package me.dexrn.rrdiscordbridge.forge;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.SupportedFeatures;
import me.dexrn.rrdiscordbridge.config.ConfigDirectory;
import me.dexrn.rrdiscordbridge.forge.impls.*;
import me.dexrn.rrdiscordbridge.impls.logging.Log4JLogger;
import me.dexrn.rrdiscordbridge.mc.multiversion.modern.AbstractModernMinecraftMod;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import org.apache.logging.log4j.LogManager;

public class ForgeColorMod extends AbstractModernMinecraftMod {
    public ForgeColorMod(Semver minecraftVer) {
        super(minecraftVer);
        EVENT_BUS.register(this);
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                            }
                        });
    }

    @Override
    public void init(MinecraftServer server) {
        EVENT_BUS.register(
                new ForgeColorEventHandler<>(ForgeColorServer::new, ForgeColorPlayer::new));

        ICommandManager m = server.getCommandManager();

        if (m instanceof CommandHandler)
            new ForgeColorMinecraftCommands().register((CommandHandler) m);
    }

    @Override
    public void preInit() {
        ForgeColorServerHandler.onServerStarting(
                FMLCommonHandler.instance().getMinecraftServerInstance(), this);
    }

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new ForgeColorServer(server),
                        new Log4JLogger(LogManager.getLogger("RRDiscordBridge")),
                        ConfigDirectory.MOD,
                        MinecraftServer.class.getClassLoader());

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
