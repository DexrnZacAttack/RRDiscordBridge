package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEvent {
    Event<AdvancementAwardEvent> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEvent.class,
                    (listeners) ->
                            (player, advancement) -> {
                                for (AdvancementAwardEvent listener : listeners) {
                                    listener.onAdvancementAward(player, advancement);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, AdvancementHolder advancement);
}
