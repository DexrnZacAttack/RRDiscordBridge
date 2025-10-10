package me.dexrn.rrdiscordbridge.extension.event;

/** Event handler */
@FunctionalInterface
public interface IExtensionEventHandler<T extends ExtensionEvent> {
    void invoke(T event);
}
