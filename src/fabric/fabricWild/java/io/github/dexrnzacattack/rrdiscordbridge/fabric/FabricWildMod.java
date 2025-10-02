package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.AdvancementAwardEventWild;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEventWild;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricWildPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricWildServer;
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

public class FabricWildMod implements IFabricMod {
    @Override
    public void init(MinecraftServer server, Semver mcVer) {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                (m, p, t) -> {
                    if (m.filtered() == null) return true;

                    String message = m.filtered().signedContent().getString();
                    Cancellable c = new Cancellable();

                    Events.onChatMessage(new FabricWildPlayer(p), message, c);

                    return !c.isCancelled();
                });

        PlayerCommandEventWild.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricWildPlayer(p), "/" + s);
                });

        AdvancementAwardEventWild.EVENT.register(
                (p, a) -> {
                    DisplayInfo info = a.getDisplay();

                    if (info == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(info.getFrame().getName()),
                            new FabricWildPlayer(p),
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
                        new FabricWildServer(server),
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
