package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerChatEvent {
    Event<PlayerChatEvent> EVENT =
            EventFactory.createArrayBacked(
                    PlayerChatEvent.class,
                    (listeners) ->
                            (player, message, ci) -> {
                                for (PlayerChatEvent listener : listeners) {
                                    listener.onPlayerChat(player, message, ci);
                                }
                            });

    void onPlayerChat(ServerPlayer player, String message, CallbackInfo ci);
}
