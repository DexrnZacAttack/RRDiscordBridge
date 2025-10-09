package io.github.dexrnzacattack.rrdiscordbridge.extension.event;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/** Event type, holds the event name and the event class */
public class ExtensionEventType<T extends ExtensionEvent> {
    private final String name;
    private final Class<T> event;

    public ExtensionEventType(String name, Class<T> event) {
        this.name = name;
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public Class<T> getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(name)
                .append(event)
                .toString();
    }
}
