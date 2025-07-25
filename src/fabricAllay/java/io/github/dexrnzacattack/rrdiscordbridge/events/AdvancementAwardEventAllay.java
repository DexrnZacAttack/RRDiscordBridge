package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEventAllay {
    Event<AdvancementAwardEventAllay> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEventAllay.class,
                    (listeners) ->
                            (player, advancement) -> {
                                for (AdvancementAwardEventAllay listener : listeners) {
                                    listener.onAdvancementAward(player, advancement);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, Advancement advancement);
}
