package io.github.dexrnzacattack.rrdiscordbridge.neoforge;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;
import io.github.dexrnzacattack.rrdiscordbridge.neoforge.impls.NeoForgePlayer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

public class NeoForgeTradeEventHandler {
    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    new NeoForgePlayer((ServerPlayer) event.getEntity()),
                    adv.name().orElse(Component.literal("")).getString(),
                    info.getDescription().getString());
        }
    }
}
