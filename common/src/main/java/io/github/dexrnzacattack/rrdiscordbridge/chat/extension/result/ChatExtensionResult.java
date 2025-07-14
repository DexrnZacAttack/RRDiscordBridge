package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result;

public class ChatExtensionResult {
    public String string;
    public boolean sendMc;
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
