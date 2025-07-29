package io.github.dexrnzacattack.rrdiscordbridge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEventNether;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEventNether;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.*;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.advancements.DisplayInfo;
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

        PlayerCommandEventNether.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricNetherPlayer(p), "/" + s);
                });

        AdvancementAwardEventNether.EVENT.register(
                (p, a) -> {
                    DisplayInfo info = a.getDisplay();

                    if (info == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(info.getFrame().getName()),
                            new FabricNetherPlayer(p),
                            info.getTitle().getString(),
                            info.getDescription().getString());
                });
    }

    @Override
    public void preInit() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ded) -> ModernMinecraftCommands.register(dispatcher));
    }
}
