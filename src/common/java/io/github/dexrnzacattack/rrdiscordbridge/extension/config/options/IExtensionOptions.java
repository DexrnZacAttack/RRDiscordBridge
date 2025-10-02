package io.github.dexrnzacattack.rrdiscordbridge.extension.config.options;

import com.google.gson.GsonBuilder;
import com.vdurmont.semver4j.Semver;

/** Extension options base interface */
public interface IExtensionOptions {
    /** Upgrades the extension's options */
    default IExtensionOptions upgrade(Semver latestVersion, Semver currentVersion) {
        return this;
    }

    /** Allows for adding onto the {@link GsonBuilder} before (de)serialization */
    default GsonBuilder configGsonBuilder(GsonBuilder builder) {
        return builder;
    }
}
