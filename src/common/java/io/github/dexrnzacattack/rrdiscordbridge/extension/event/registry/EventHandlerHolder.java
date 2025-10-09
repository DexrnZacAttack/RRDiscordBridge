package io.github.dexrnzacattack.rrdiscordbridge.extension.event.registry;

import io.github.dexrnzacattack.rrdiscordbridge.extension.AbstractBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEventType;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.IExtensionEventHandler;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

/** Wraps an event handler  and provides the handler, event type it applies to, and the owner extension */
public class EventHandlerHolder<T extends ExtensionEvent> {
    /** The event type the handler applies to */
    private final ExtensionEventType<T> type;
    /** The event handler method */
    private final IExtensionEventHandler<T> handler;
    /** The owner extension */
    private final AbstractBridgeExtension owner;

    public EventHandlerHolder(AbstractBridgeExtension owner, ExtensionEventType<T> type, IExtensionEventHandler<T> handler) {
        this.owner = owner;
        this.type = type;
        this.handler = handler;
    }

    /** Gets the extension class that created the handler */
    public AbstractBridgeExtension getOwner() {
        return owner;
    }

    /** Gets the type of event the handler applies to */
    public ExtensionEventType<T> getType() {
        return type;
    }

    /** Gets the name of the owner extension class */
    public String getOwnerName() {
        return owner.getName();
    }

    /** Gets the handler */
    public IExtensionEventHandler<T> getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(type)
                .append(owner)
                .append(handler)
                .toString();
    }
}
