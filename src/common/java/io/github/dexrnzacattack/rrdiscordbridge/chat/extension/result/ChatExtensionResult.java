package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result;

/** Result of the chat extension */
public class ChatExtensionResult {
    /** The string to send */
    public String string;

    /** Whether to send to Minecraft chat */
    public boolean sendMc;

    /** Whether to send to Discord chat */
    public boolean sendDiscord;

    public ChatExtensionResult(String string, boolean sendMc, boolean sendDiscord) {
        this.string = string;
        this.sendMc = sendMc;
        this.sendDiscord = sendDiscord;
    }

    @Override
    public String toString() {
        return String.format("%s (mc: %b, dc: %b)", string, sendMc, sendDiscord);
    }
}
