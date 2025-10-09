package io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat;

import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.types.ModifiableMessage;
import net.dv8tion.jda.api.entities.Message;

/**
 * Runs when a message is sent in the Discord server
 *
 * <p>It's global so that things like OpChat can work properly (sends and receives messages from
 * different channel)
 */
public class DiscordChatEvent extends ExtensionEvent {
    private final ModifiableMessage<Message> result;

    public DiscordChatEvent(ModifiableMessage<Message> result) {
        this.result = result;
    }

    public DiscordChatEvent(Message message) {
        this.result = new ModifiableMessage<>(message);
    }

    public Message getMessage() {
        return result.message;
    }

    public ModifiableMessage<Message> getResult() {
        return result;
    }
}