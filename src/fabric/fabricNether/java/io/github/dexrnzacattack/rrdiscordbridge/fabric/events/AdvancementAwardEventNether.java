package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEventNether {
    Event<AdvancementAwardEventNether> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEventNether.class,
                    (listeners) ->
                            (player, advancement) -> {
                                for (AdvancementAwardEventNether listener : listeners) {
                                    listener.onAdvancementAward(player, advancement);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, Advancement advancement);
}
