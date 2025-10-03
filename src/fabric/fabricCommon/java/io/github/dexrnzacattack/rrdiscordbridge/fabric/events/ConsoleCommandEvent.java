package io.github.dexrnzacattack.rrdiscordbridge.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface ConsoleCommandEvent {
    Event<ConsoleCommandEvent> EVENT =
            EventFactory.createArrayBacked(
                    ConsoleCommandEvent.class,
                    (listeners) ->
                            (message, ci) -> {
                                for (ConsoleCommandEvent listener : listeners) {
                                    listener.onConsoleCommand(message, ci);
                                }
                            });

    void onConsoleCommand(String message, CallbackInfo ci);
}
