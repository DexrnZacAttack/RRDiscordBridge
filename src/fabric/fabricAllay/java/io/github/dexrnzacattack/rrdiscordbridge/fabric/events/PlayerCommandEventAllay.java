package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerCommandEventAllay {
    Event<PlayerCommandEventAllay> EVENT =
            EventFactory.createArrayBacked(
                    PlayerCommandEventAllay.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerCommandEventAllay listener : listeners) {
                                    listener.onPlayerCommand(player, message, ci);
                                }
                            });

    void onPlayerCommand(ServerPlayer player, String message, CallbackInfo ci);
}
