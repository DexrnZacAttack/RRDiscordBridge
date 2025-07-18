package io.github.dexrnzacattack.rrdiscordbridge;

import static io.github.dexrnzacattack.rrdiscordbridge.command.CommandRegistry.CommandName.*;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEvent;
import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeathEvent;
import io.github.dexrnzacattack.rrdiscordbridge.impls.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;

import org.slf4j.LoggerFactory;

public class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // is this a good idea? Only way I can get access to a MinecraftServer instance, but also
        // delays the mod loading until the server has loaded the world.
        ServerLifecycleEvents.SERVER_STARTING.register(
                server -> {
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

                    ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
                            (t, p, b) -> {
                                Cancellable c = new Cancellable();

                                Events.onChatMessage(new FabricPlayer(p), t.signedContent(), c);

                                return !c.isCancelled();
                            });

                    ServerPlayerEvents.JOIN.register(t -> Events.onPlayerJoin(new FabricPlayer(t)));

                    ServerPlayerEvents.LEAVE.register(
                            t -> Events.onPlayerLeave(new FabricPlayer(t)));

                    ServerLifecycleEvents.SERVER_STOPPED.register(
                            t -> RRDiscordBridge.instance.shutdown());

                    AdvancementAwardEvent.EVENT.register(
                            (p, a) -> {
                                Advancement adv = a.value();
                                DisplayInfo info = adv.display().orElse(null);

                                if (info == null) return;

                                Events.onPlayerAchievement(
                                        AdvancementType.getType(info.getType()),
                                        new FabricPlayer(p),
                                        adv.name().orElse(Component.literal("")).getString());
                            });

                    PlayerDeathEvent.EVENT.register(
                            (p, c) -> Events.onPlayerDeath(new FabricPlayer(p), c.getString()));
                });

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    ModernMinecraftCommands.register(dispatcher);
                });
    }
}
