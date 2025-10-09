package io.github.dexrnzacattack.rrdiscordbridge.extension.loader;

import io.github.dexrnzacattack.rrdiscordbridge.extension.BridgeExtensionManager;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.internal.BridgeExtensionsRegisterEvent;

/** Loads extensions that have added themselves onto our internal event */
public class EventExtensionLoader implements IExtensionLoader {
    @Override
    public void registerExtensions(BridgeExtensionManager manager) {
        BridgeExtensionsRegisterEvent.registerAll(manager);
    }
}
