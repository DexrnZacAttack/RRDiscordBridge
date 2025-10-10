package me.dexrn.rrdiscordbridge.extension.loader;

import me.dexrn.rrdiscordbridge.extension.BridgeExtensionManager;

/** Interface for loading and registering extensions */
public interface IExtensionLoader {
    void registerExtensions(BridgeExtensionManager manager);
}
