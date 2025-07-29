package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result;

import net.dv8tion.jda.api.entities.Message;

public class DiscordChatExtensionResult {
    /** The resulting message content */
    public Message message;

    /** Whether to send to Minecraft chat */
    public boolean sendMc;

    public DiscordChatExtensionResult(Message message, boolean sendMc) {
        this.message = message;
        this.sendMc = sendMc;
    }

    @Override
    public String toString() {
        return String.format("%s (mc: %b)", message.toString(), sendMc);
    }
}
