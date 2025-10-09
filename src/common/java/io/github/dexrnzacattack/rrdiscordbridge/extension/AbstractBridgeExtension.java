package io.github.dexrnzacattack.rrdiscordbridge.extension;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/** Base extension class */
public abstract class AbstractBridgeExtension implements IBridgeExtension {
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("name", getName())
                .append("author", getAuthor())
                .append("description", getDescription())
                .toString();
    }
}
