package io.github.dexrnzacattack.rrdiscordbridge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEventTrails;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEventTrails;
import io.github.dexrnzacattack.rrdiscordbridge.fabric.IFabricMod;
import io.github.dexrnzacattack.rrdiscordbridge.impls.*;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import org.slf4j.LoggerFactory;

public class FabricTrailsMod implements IFabricMod {
    @Override
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricTrailsServer(server),
                        new SLF4JLogger(LoggerFactory.getLogger("RRDiscordBridge")),
                        ConfigDirectory.MOD.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(true)
                        .setCanGetServerName(false)
                        .setCanQueryServerOperators(true)
                        .setCanQueryPlayerHasJoinedBefore(false));
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
                        Events.onPlayerCommand(new FabricTrailsPlayer(p), t.signedContent());
                    else Events.onChatMessage(new FabricTrailsPlayer(p), t.signedContent(), c);

                    return !c.isCancelled();
                });

        PlayerCommandEventTrails.EVENT.register(
                (p, s, c) -> {
                    Events.onPlayerCommand(new FabricTrailsPlayer(p), "/" + s);
                });

        AdvancementAwardEventTrails.EVENT.register(
                (p, a, i) -> {
                    Advancement adv = a.value();
                    DisplayInfo info = adv.display().orElse(null);

                    String description = null;
                    if (info != null) description = info.getDescription().getString();

                    Events.onPlayerAchievement(
                            AdvancementType.getType(i.getType()),
                            new FabricTrailsPlayer(p),
                            adv.name().orElse(Component.literal("")).getString(),
                            description);
                });
    }
}
