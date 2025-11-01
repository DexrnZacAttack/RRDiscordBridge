package me.dexrn.rrdiscordbridge.neoforge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.mc.impls.AbstractEventHandler;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.advancement.AdvancementType;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgePlayer;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeServer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

import java.util.function.Function;

public class NeoForgeTradeEventHandler<S extends NeoForgeServer, P extends NeoForgePlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, ServerPlayer> {

    public NeoForgeTradeEventHandler(
            Function<MinecraftServer, S> server, Function<ServerPlayer, P> player) {
        super(server, player);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    createPlayer((ServerPlayer) event.getEntity()),
                    adv.name().orElse(Component.literal("")).getString(),
                    info.getDescription().getString());
        }
    }
}
