package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEvent1201 {
    Event<AdvancementAwardEvent1201> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEvent1201.class,
                    (listeners) ->
                            (player, advancement) -> {
                                for (AdvancementAwardEvent1201 listener : listeners) {
                                    listener.onAdvancementAward(player, advancement);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, Advancement advancement);
}
