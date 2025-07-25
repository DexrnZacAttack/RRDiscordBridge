package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerDeathEvent {
    Event<PlayerDeathEvent> EVENT =
            EventFactory.createArrayBacked(
                    PlayerDeathEvent.class,
                    (listeners) ->
                            (player, message) -> {
                                for (PlayerDeathEvent listener : listeners) {
                                    listener.onPlayerDeath(player, message);
                                }
                            });

    void onPlayerDeath(ServerPlayer player, Component message);
}
