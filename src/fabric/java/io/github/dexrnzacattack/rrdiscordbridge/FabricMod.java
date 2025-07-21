package io.github.dexrnzacattack.rrdiscordbridge;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeathEvent;
import io.github.dexrnzacattack.rrdiscordbridge.impls.*;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import org.slf4j.LoggerFactory;

public class FabricMod implements ModInitializer {
    public void setupBridge(MinecraftServer server) {
        // ctor
        RRDiscordBridge.instance =
                new RRDiscordBridge(
                        new FabricServer(server),
                        new SLF4JLogger(LoggerFactory.getLogger("RRDiscordBridge")),
                        ConfigDirectory.MOD.getPath());

        // then we init
        RRDiscordBridge.instance.initialize();
        RRDiscordBridge.instance.setSupportedFeatures(
                new SupportedFeatures()
                        .setCanGetServerMotd(true)
                        .setCanGetServerName(false)
                        .setCanQueryServerOperators(true));
    }

    @Override
    public void onInitialize() {
        String v =
                FabricLoader.getInstance()
                        .getModContainer("minecraft")
                        .get()
                        .getMetadata()
                        .getVersion()
                        .getFriendlyString();

        Semver mcVer = new Semver(v, Semver.SemverType.LOOSE);

        // is this a good idea? Only way I can get access to a MinecraftServer instance, but also
        // delays the mod loading until the server has loaded the world.
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
                    if (mcVer.isLowerThan("1.19")) Fabric1201Mod.setupBridge(server);
                    else setupBridge(server);

                    // init older fabric mod impl
                    if (mcVer.isLowerThan("1.20.2")) {
                        Fabric1201Mod.init(this, server);
                    } else {
                        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                                (t, p, b) -> {
                                    Cancellable c = new Cancellable();

                                    if (t.signedContent().startsWith("/"))
                                        Events.onPlayerCommand(
                                                new Fabric1201Player(p), t.signedContent());
                                    else
                                        Events.onChatMessage(
                                                new FabricPlayer(p), t.signedContent(), c);

                                    return !c.isCancelled();
                                });

                        AdvancementAwardEvent.EVENT.register(FabricMod::onAdvancement);

                        CommandRegistrationCallback.EVENT.register(
                                (dispatcher, ctx, selection) -> {
                                    ModernMinecraftCommands.register(dispatcher);
                                });
                    }

                    ServerPlayConnectionEvents.JOIN.register(
                            (i, s, mcs) -> Events.onPlayerJoin(new FabricPlayer(i.player)));

                    ServerPlayConnectionEvents.DISCONNECT.register(
                            (i, s) -> Events.onPlayerLeave(new FabricPlayer(i.player)));

                    ServerLifecycleEvents.SERVER_STOPPED.register(
                            t -> RRDiscordBridge.instance.shutdown());

                    PlayerDeathEvent.EVENT.register(
                            (p, c) -> Events.onPlayerDeath(new FabricPlayer(p), c.getString()));
                });
    }

    public static void onAdvancement(ServerPlayer p, AdvancementHolder a) {
        Advancement adv = a.value();
        DisplayInfo info = adv.display().orElse(null);

        if (info == null) return;

        Events.onPlayerAchievement(
                AdvancementType.getType(info.getType()),
                new FabricPlayer(p),
                adv.name().orElse(Component.literal("")).getString());
    }
}
