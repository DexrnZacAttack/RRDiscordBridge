package me.dexrn.rrdiscordbridge.neoforge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgePlayer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

public class NeoForgePotEventHandler {
    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getType(info.getType()),
                    new NeoForgePlayer((ServerPlayer) event.getEntity()),
                    adv.name().orElse(Component.literal("")).getString(),
                    info.getDescription().getString());
        }
    }
}
