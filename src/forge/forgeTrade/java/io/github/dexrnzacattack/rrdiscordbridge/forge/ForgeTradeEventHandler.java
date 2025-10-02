package io.github.dexrnzacattack.rrdiscordbridge.forge;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgePlayer;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTradeEventHandler {
    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    new ForgePlayer((ServerPlayer) event.getEntity()),
                    adv.name().orElse(Component.literal("")).getString(),
                    info.getDescription().getString());
        }
    }
}
