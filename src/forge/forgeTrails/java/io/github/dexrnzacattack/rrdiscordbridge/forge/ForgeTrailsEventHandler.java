package io.github.dexrnzacattack.rrdiscordbridge.forge;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgePlayer;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTrailsEventHandler {
    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement();
        DisplayInfo info = adv.getDisplay();

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    new ForgePlayer((ServerPlayer) event.getEntity()),
                    info.getTitle().getString(),
                    info.getDescription().getString());
        }
    }
}
