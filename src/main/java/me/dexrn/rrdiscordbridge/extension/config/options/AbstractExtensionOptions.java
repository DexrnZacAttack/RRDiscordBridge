package me.dexrn.rrdiscordbridge.extension.config.options;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/** The base extension options class, extension options must extend this to work. */
public abstract class AbstractExtensionOptions implements IExtensionOptions {
    /** The extension type, used for deserializing, do not mess with this field. */
    @Expose() public String type = "Unknown";

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(type).toString();
    }
}
