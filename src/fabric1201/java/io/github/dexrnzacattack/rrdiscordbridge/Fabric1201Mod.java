package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEvent1201;
import io.github.dexrnzacattack.rrdiscordbridge.events.ConsoleCommandEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEvent;
import io.github.dexrnzacattack.rrdiscordbridge.impls.CancellableCallbackInfo;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Fabric1201Player;
import io.github.dexrnzacattack.rrdiscordbridge.impls.Fabric1201Server;
import io.github.dexrnzacattack.rrdiscordbridge.impls.JavaLogger;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.logging.Logger;

public class Fabric1201Mod {
    public static void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new Fabric1201Server(server),
                        new JavaLogger(Logger.getLogger("RRDiscordBridge")), // TODO: slf4j
                        ConfigDirectory.MOD.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(true)
                        .setCanGetServerName(false)
                        .setCanQueryServerOperators(true));
    }

    // UNFINISHED
    public static void init(ModInitializer self, MinecraftServer server) {
        PlayerChatEvent.EVENT.register(
                (p, m, c) -> {
                    Events.onChatMessage(
                            new Fabric1201Player(p), m, new CancellableCallbackInfo(c));
                });

        PlayerCommandEvent.EVENT.register(
                (p, m, c) -> {
                    RRDiscordBridge.instance.getLogger().info(p.getName().getString());
                    RRDiscordBridge.instance.getLogger().info(p.getStringUUID());
                    Events.onPlayerCommand(new Fabric1201Player(p), m);
                });

        ConsoleCommandEvent.EVENT.register(
                (m, c) -> {
                    Events.onServerCommand(m);
                });

        AdvancementAwardEvent1201.EVENT.register(Fabric1201Mod::onAdvancement);

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, ded) -> {
                    ModernMinecraftCommands.register(dispatcher);
                });
    }

    public static void onAdvancement(ServerPlayer p, Advancement a) {
        DisplayInfo info = a.getDisplay();

        if (info == null) return;

        Events.onPlayerAchievement(
                AdvancementType.getTypeFromName(info.getFrame().getName()),
                new Fabric1201Player(p),
                info.getTitle().getString());
    }
}
