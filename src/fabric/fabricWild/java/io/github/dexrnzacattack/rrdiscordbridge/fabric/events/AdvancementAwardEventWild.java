package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEventWild {
    Event<AdvancementAwardEventWild> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEventWild.class,
                    (listeners) ->
                            (player, advancement) -> {
                                for (AdvancementAwardEventWild listener : listeners) {
                                    listener.onAdvancementAward(player, advancement);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, Advancement advancement);
}
