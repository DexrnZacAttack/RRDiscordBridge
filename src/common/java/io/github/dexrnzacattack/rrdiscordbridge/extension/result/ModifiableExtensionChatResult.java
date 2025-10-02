package io.github.dexrnzacattack.rrdiscordbridge.extension.result;

import io.github.dexrnzacattack.rrdiscordbridge.impls.Cancellable;

/** Result of the extension */
public class ModifiableExtensionChatResult<T> {
    /** The string to send */
    public T message;

    /** Whether to send to Minecraft chat */
    private final Cancellable minecraftCancellable = new Cancellable();

    /** Whether to send to Discord chat */
    private final Cancellable discordCancellable = new Cancellable();

    public ModifiableExtensionChatResult(T message) {
        this.message = message;
    }

    /** Disallows (cancels) sending the message to Minecraft chat */
    public ModifiableExtensionChatResult<T> cancelSendToMinecraft() {
        this.minecraftCancellable.cancel();
        return this;
    }

    /** Allows (uncancels) sending the message to Minecraft chat */
    public ModifiableExtensionChatResult<T> uncancelSendToMinecraft() {
        this.discordCancellable.uncancel();
        return this;
    }

    /** Disallows (cancels) sending the message to Discord */
    public ModifiableExtensionChatResult<T> cancelSendToDiscord() {
        this.minecraftCancellable.cancel();
        return this;
    }

    /** Allows (uncancels) sending the message to Discord */
    public ModifiableExtensionChatResult<T> uncancelSendToDiscord() {
        this.discordCancellable.uncancel();
        return this;
    }

    public boolean getShouldSendToDiscord() {
        return !this.discordCancellable.isCancelled();
    }

    public boolean getShouldSendToMinecraft() {
        return !this.minecraftCancellable.isCancelled();
    }

    @Override
    public String toString() {
        return String.format(
                "%s (mc: %b, dc: %b)",
                message, minecraftCancellable.isCancelled(), discordCancellable.isCancelled());
    }
}
