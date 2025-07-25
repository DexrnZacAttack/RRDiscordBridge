package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerCommandEventWild {
    Event<PlayerCommandEventWild> EVENT =
            EventFactory.createArrayBacked(
                    PlayerCommandEventWild.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerCommandEventWild listener : listeners) {
                                    listener.onPlayerCommand(player, message, ci);
                                }
                            });

    void onPlayerCommand(ServerPlayer player, String message, CallbackInfo ci);
}
