package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEvent {
    Event<AdvancementAwardEvent> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEvent.class,
                    (listeners) ->
                            (player, advancement, info) -> {
                                for (AdvancementAwardEvent listener : listeners) {
                                    listener.onAdvancementAward(player, advancement, info);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, Advancement advancement, DisplayInfo info);
}
