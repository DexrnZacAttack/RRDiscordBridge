package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.AdvancementAwardEventVex;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEventVex;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricVexPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricVexServer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.multiversion.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.SLF4JLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.MinecraftServer;

import org.slf4j.LoggerFactory;

public class FabricVexMod implements IFabricMod {
    @Override
    public void init(MinecraftServer server, Semver minecraftVersion) {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                (t, p, b) -> {
                    Cancellable c = new Cancellable();
                    String message = t.signedContent();

                    Events.onChatMessage(new FabricVexPlayer(p), message, c);

                    return !c.isCancelled();
                });

        PlayerCommandEventVex.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricVexPlayer(p), "/" + s);
                });

        AdvancementAwardEventVex.EVENT.register(
                (p, a) -> {
                    DisplayInfo info = a.getDisplay();

                    if (info == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(info.getFrame().getName()),
                            new FabricVexPlayer(p),
                            info.getTitle().getString(),
                            info.getDescription().getString());
                });
    }

    @Override
    public void preInit() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ctx, selection) -> ModernMinecraftCommands.register(dispatcher));
    }

    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricVexServer(server),
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
