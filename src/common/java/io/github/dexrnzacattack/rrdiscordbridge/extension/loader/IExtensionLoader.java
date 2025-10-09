package io.github.dexrnzacattack.rrdiscordbridge.extension.loader;

import io.github.dexrnzacattack.rrdiscordbridge.extension.BridgeExtensionManager;

/** Interface for loading and registering extensions */
public interface IExtensionLoader {
    void registerExtensions(BridgeExtensionManager manager);
}
