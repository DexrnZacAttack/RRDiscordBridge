package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerCommandEvent {
    Event<PlayerCommandEvent> EVENT =
            EventFactory.createArrayBacked(
                    PlayerCommandEvent.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerCommandEvent listener : listeners) {
                                    listener.onPlayerCommand(player, message, ci);
                                }
                            });

    void onPlayerCommand(ServerPlayer player, String message, CallbackInfo ci);
}
