package io.github.dexrnzacattack.rrdiscordbridge.extension.config.options;

import com.google.gson.annotations.Expose;

/** The base extension options class, extension options must extend this to work. */
public abstract class BaseExtensionOptions implements IExtensionOptions {
    /** The extension type, used for deserializing, do not mess with this field. */
    @Expose() public String type = "Unknown";
}
