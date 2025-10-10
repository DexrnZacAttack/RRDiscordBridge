package me.dexrn.rrdiscordbridge.extension.types;

import me.dexrn.rrdiscordbridge.impls.Cancellable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/** Modifiable message type */
public class ModifiableMessage<T> {
    /** The string to send */
    public T message;

    /** Whether to send to Minecraft chat */
    private final Cancellable minecraftCancellable = new Cancellable();

    /** Whether to send to Discord chat */
    private final Cancellable discordCancellable = new Cancellable();

    public ModifiableMessage(T message) {
        this.message = message;
    }

    /** Disallows (cancels) sending the message to Minecraft chat */
    public ModifiableMessage<T> cancelSendToMinecraft() {
        this.minecraftCancellable.cancel();
        return this;
    }

    /** Allows (uncancels) sending the message to Minecraft chat */
    public ModifiableMessage<T> uncancelSendToMinecraft() {
        this.minecraftCancellable.uncancel();
        return this;
    }

    /** Disallows (cancels) sending the message to Discord */
    public ModifiableMessage<T> cancelSendToDiscord() {
        this.discordCancellable.cancel();
        return this;
    }

    /** Allows (uncancels) sending the message to Discord */
    public ModifiableMessage<T> uncancelSendToDiscord() {
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("toMinecraft", getShouldSendToMinecraft())
                .append("toDiscord", getShouldSendToDiscord())
                .append(message)
                .toString();
    }
}
