package io.github.dexrnzacattack.rrdiscordbridge.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerCommandEventTrails {
    Event<PlayerCommandEventTrails> EVENT =
            EventFactory.createArrayBacked(
                    PlayerCommandEventTrails.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerCommandEventTrails listener : listeners) {
                                    listener.onPlayerCommand(player, message, ci);
                                }
                            });

    void onPlayerCommand(ServerPlayer player, String message, CallbackInfo ci);
}
