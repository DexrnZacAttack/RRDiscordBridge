package io.github.dexrnzacattack.rrdiscordbridge.fabric;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.SupportedFeatures;
import io.github.dexrnzacattack.rrdiscordbridge.config.ConfigDirectory;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.AdvancementAwardEventTrade;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEventTrade;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricTradePlayer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.impls.FabricTradeServer;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.multiversion.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.logging.SLF4JLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import org.slf4j.LoggerFactory;

public class FabricTradeMod implements IFabricMod {
    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricTradeServer(server),
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

    @Override
    public void preInit() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ctx, selection) -> ModernMinecraftCommands.register(dispatcher));
    }

    @Override
    public void init(MinecraftServer server, Semver version) {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                (t, p, b) -> {
                    Cancellable c = new Cancellable();

                    if (t.signedContent().startsWith("/"))
                        Events.onPlayerCommand(new FabricTradePlayer(p), t.signedContent());
                    else Events.onChatMessage(new FabricTradePlayer(p), t.signedContent(), c);

                    return !c.isCancelled();
                });

        PlayerCommandEventTrade.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricTradePlayer(p), "/" + s);
                });

        AdvancementAwardEventTrade.EVENT.register(
                (p, a, i) -> {
                    Advancement adv = a.value();
                    DisplayInfo info = adv.display().orElse(null);

                    String description = null;
                    if (info != null) description = info.getDescription().getString();

                    Events.onPlayerAchievement(
                            AdvancementType.getType(i.getType()),
                            new FabricTradePlayer(p),
                            adv.name().orElse(Component.literal("")).getString(),
                            description);
                });
    }
}
