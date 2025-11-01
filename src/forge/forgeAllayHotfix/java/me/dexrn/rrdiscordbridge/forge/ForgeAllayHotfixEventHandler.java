package me.dexrn.rrdiscordbridge.forge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.forge.impls.ForgePlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeServer;
import me.dexrn.rrdiscordbridge.mc.impls.AbstractEventHandler;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.advancement.AdvancementType;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Function;

public class ForgeAllayHotfixEventHandler<S extends ForgeServer, P extends ForgePlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, ServerPlayer> {
    public ForgeAllayHotfixEventHandler(
            Function<MinecraftServer, S> server, Function<ServerPlayer, P> player) {
        super(server, player);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement();
        DisplayInfo info = adv.getDisplay();

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    createPlayer((ServerPlayer) event.getEntity()),
                    info.getTitle().getString(),
                    info.getDescription().getString());
        }
    }
}
