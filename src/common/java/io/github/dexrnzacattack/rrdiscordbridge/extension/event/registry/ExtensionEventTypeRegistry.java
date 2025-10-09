package io.github.dexrnzacattack.rrdiscordbridge.extension.event.registry;

import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEventType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Holds all the event types */
public class ExtensionEventTypeRegistry {
    /** Holds a map of names to their events */
    private final Map<String, ExtensionEventType<? extends ExtensionEvent>> types = new HashMap<>();

    private ExtensionEventTypeRegistry() {}

    /** Gets the static instance created at startup */
    public static ExtensionEventTypeRegistry getInstance() {
        return INSTANCE;
    }

    /** Registers an event type */
    public synchronized <T extends ExtensionEvent> ExtensionEventType<T> register(String name, Class<T> clazz) {
        if (types.containsKey(name))
            throw new IllegalArgumentException(String.format("Event type '%s' already exists!", name));

        ExtensionEventType<T> t = new ExtensionEventType<>(name, clazz);
        types.put(name, t);

        return t;
    }

    /** Returns an event type by name */
    public ExtensionEventType<? extends ExtensionEvent> get(String name) {
        return types.get(name);
    }

    // too much generic hell
    /** Returns a collection of all event types */
    public Collection<ExtensionEventType<? extends ExtensionEvent>> getAll() {
        return Collections.unmodifiableCollection(types.values());
    }

    /** Instance created at runtime */
    private static final ExtensionEventTypeRegistry INSTANCE = new ExtensionEventTypeRegistry();
}
