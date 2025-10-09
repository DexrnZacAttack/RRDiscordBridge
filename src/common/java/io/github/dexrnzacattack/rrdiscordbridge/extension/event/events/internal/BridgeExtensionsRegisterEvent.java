package io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.internal;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.BridgeExtensionManager;
import io.github.dexrnzacattack.rrdiscordbridge.extension.AbstractBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.ExtensionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for events to be registered from sources such as other plugins/mods or internally within
 * RRDiscordBridge
 */
public class BridgeExtensionsRegisterEvent {
    public static boolean hasRegistered = false;
    private static final List<Class<? extends AbstractBridgeExtension>> waiting = new ArrayList<>();

    public static void register(Class<? extends AbstractBridgeExtension> ext) {
        if (!hasRegistered) waiting.add(ext);
        else RRDiscordBridge.instance.getBridgeExtensions().register(ext);
    }

    public static void registerAll(BridgeExtensionManager extensions) {
        hasRegistered = true;
        for (Class<? extends AbstractBridgeExtension> ext : waiting) {
            extensions.register(ext);
        }

        waiting.clear();
    }
}
