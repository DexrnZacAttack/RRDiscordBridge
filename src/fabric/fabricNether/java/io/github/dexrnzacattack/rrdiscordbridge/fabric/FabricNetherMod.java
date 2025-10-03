package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.AdvancementAwardEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.CancellableCallbackInfo;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricNetherPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricNetherServer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.multiversion.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.Log4JLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;

public class FabricNetherMod implements IFabricMod {
    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricNetherServer(server),
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

    // UNFINISHED
    @Override
    public void init(MinecraftServer server, Semver mcVer) {
        PlayerChatEvent.EVENT.register(
                (p, m, c) -> {
                    Events.onChatMessage(
                            new FabricNetherPlayer(p), m, new CancellableCallbackInfo(c));
                });

        PlayerCommandEvent.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricNetherPlayer(p), "/" + s);
                });

        AdvancementAwardEvent.EVENT.register(
                (p, a, d) -> {
                    if (d == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(d.getFrame().getName()),
                            new FabricNetherPlayer(p),
                            d.getTitle().getString(),
                            d.getDescription().getString());
                });
    }

    @Override
    public void preInit() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ded) -> ModernMinecraftCommands.register(dispatcher));
    }
}
