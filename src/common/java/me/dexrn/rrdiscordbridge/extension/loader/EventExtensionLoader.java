package me.dexrn.rrdiscordbridge.extension.loader;

import me.dexrn.rrdiscordbridge.extension.BridgeExtensionManager;
import me.dexrn.rrdiscordbridge.extension.event.events.internal.BridgeExtensionsRegisterEvent;

/** Loads extensions that have added themselves onto our internal event */
public class EventExtensionLoader implements IExtensionLoader {
    @Override
    public void registerExtensions(BridgeExtensionManager manager) {
        BridgeExtensionsRegisterEvent.registerAll(manager);
    }
}
