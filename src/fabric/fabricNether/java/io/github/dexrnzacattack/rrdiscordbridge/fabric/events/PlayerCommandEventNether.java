package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerCommandEventNether {
    Event<PlayerCommandEventNether> EVENT =
            EventFactory.createArrayBacked(
                    PlayerCommandEventNether.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerCommandEventNether listener : listeners) {
                                    listener.onPlayerCommand(player, message, ci);
                                }
                            });

    void onPlayerCommand(ServerPlayer player, String message, CallbackInfo ci);
}
