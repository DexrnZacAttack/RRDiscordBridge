package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;

public interface AdvancementAwardEventTrails {
    Event<AdvancementAwardEventTrails> EVENT =
            EventFactory.createArrayBacked(
                    AdvancementAwardEventTrails.class,
                    (listeners) ->
                            (player, advancement, info) -> {
                                for (AdvancementAwardEventTrails listener : listeners) {
                                    listener.onAdvancementAward(player, advancement, info);
                                }
                            });

    void onAdvancementAward(ServerPlayer player, AdvancementHolder advancement, DisplayInfo info);
}
