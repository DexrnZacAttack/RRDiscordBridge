package io.github.dexrnzacattack.rrdiscordbridge.extension.event;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.BridgeExtensions;
import io.github.dexrnzacattack.rrdiscordbridge.extension.IBridgeExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for events to be registered from sources such as other plugins/mods or internally within
 * RRDiscordBridge
 */
public class BridgeExtensionsRegisterEvent {
    public static boolean hasRegistered = false;
    private static final List<Class<? extends IBridgeExtension>> waiting = new ArrayList<>();

    public static void register(Class<? extends IBridgeExtension> ext) {
        if (!hasRegistered) waiting.add(ext);
        else RRDiscordBridge.instance.getBridgeExtensions().register(ext);
    }

    public static void registerAll(BridgeExtensions extensions) {
        hasRegistered = true;
        for (Class<? extends IBridgeExtension> ext : waiting) {
            extensions.register(ext);
        }

        waiting.clear();
    }
}
