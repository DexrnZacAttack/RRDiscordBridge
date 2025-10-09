package io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat;

import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.types.ModifiableMessage;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

/** Runs when a message is sent in MC chat */
public class MinecraftChatEvent extends ExtensionEvent {
    private final IPlayer player;
    private final ModifiableMessage<String> result;

    public MinecraftChatEvent(IPlayer player, String message) {
        this.player = player;
        this.result = new ModifiableMessage<>(message);
    }

    public IPlayer getPlayer() {
        return player;
    }

    public String getMessage() {
        return result.message;
    }

    public void setMessage(String message) {
        result.message = message;
    }

    public ModifiableMessage<String> getResult() {
        return result;
    }
}
