package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.AdvancementAwardEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEvent;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricServer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.multiversion.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.SLF4JLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;

import org.slf4j.LoggerFactory;

public class FabricAllayMod implements IFabricMod {
    @Override
    public void init(MinecraftServer server, Semver minecraftVersion) {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                (t, p, b) -> {
                    Cancellable c = new Cancellable();
                    String message = t.signedContent().decorated().getString();

                    if (message.startsWith("/"))
                        Events.onPlayerCommand(new FabricPlayer(p), message);
                    else Events.onChatMessage(new FabricPlayer(p), message, c);

                    return !c.isCancelled();
                });

        PlayerCommandEvent.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricPlayer(p), "/" + s);
                });

        AdvancementAwardEvent.EVENT.register(
                (p, a, d) -> {
                    if (d == null) return;

                    Events.onPlayerAchievement(
                            AdvancementType.getTypeFromName(d.getFrame().getName()),
                            new FabricPlayer(p),
                            d.getTitle().getString(),
                            d.getDescription().getString());
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
                        new FabricServer(server),
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
