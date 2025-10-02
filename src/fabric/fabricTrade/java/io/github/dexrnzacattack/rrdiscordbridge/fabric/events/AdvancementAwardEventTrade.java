package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEventTrade {
    Event<AdvancementAwardEventTrade> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEventTrade.class,
                    (listeners) ->
                            (player, advancement, info) -> {
                                for (AdvancementAwardEventTrade listener : listeners) {
                                    listener.onAdvancementAward(player, advancement, info);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, AdvancementHolder advancement, DisplayInfo info);
}
