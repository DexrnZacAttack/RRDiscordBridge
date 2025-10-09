package io.github.dexrnzacattack.rrdiscordbridge.extension.event.registry;

import io.github.dexrnzacattack.rrdiscordbridge.extension.AbstractBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEventType;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.IExtensionEventHandler;

import java.util.*;

// warning: this shit makes my head hurt
/** Holds all registered events */
public class ExtensionEventRegistry {
    /** Holds a map of events to their handlers */
    private final Map<ExtensionEventType<? extends ExtensionEvent>, List<EventHandlerHolder<? extends ExtensionEvent>>> events = new HashMap<>();

    /** Gets the static instance created at startup */
    public static ExtensionEventRegistry getInstance() {
        return INSTANCE;
    }

    /** Registers an event */
    public <T extends ExtensionEvent> EventHandlerHolder<? extends ExtensionEvent> register(AbstractBridgeExtension owner, ExtensionEventType<T> type, IExtensionEventHandler<T> handler) {
        EventHandlerHolder<T> t = new EventHandlerHolder<>(owner, type, handler);
        events.computeIfAbsent(type, e -> Collections.emptyList()).add(t);

        return t;
    }

    /** Unregisters an event handler */
    public <T extends ExtensionEvent> void unregister(EventHandlerHolder<? extends ExtensionEvent> r) {
        // if only we had ?. syntax in j8
        List<EventHandlerHolder<? extends ExtensionEvent>> h = events.get(r.getType());

        if (h != null) h.remove(r);
    }

    /** Unregisters all event handers created by an extension */
    public void unregisterAll(AbstractBridgeExtension owner) {
        events.values().forEach(h -> h.removeIf(rh -> rh.getOwner().equals(owner)));
    }

    /** Invokes the event, calling all handlers */
    public <T extends ExtensionEvent> void invoke(ExtensionEventType<T> type, T event) {
        for (EventHandlerHolder<? extends ExtensionEvent> h : events.getOrDefault(type, Collections.emptyList())) {
            ((IExtensionEventHandler<T>) h.getHandler()).invoke(event); // shut uppppppp
        }
    }

    // yes I'm gonna shove instance down here
    /** Instance created at runtime */
    private static final ExtensionEventRegistry INSTANCE = new ExtensionEventRegistry();
}
