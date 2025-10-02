package io.github.dexrnzacattack.rrdiscordbridge.extension;

/**
 * Extension Priority
 *
 * <p>Changes the order which extensions are called on
 */
public enum ExtensionPriority {
    /** Runs last */
    LOW(0),
    /** Runs second */
    MEDIUM(1),
    /** Runs first */
    HIGH(2);

    private final int priority;

    ExtensionPriority(int p) {
        this.priority = p;
    }

    public int getPriority() {
        return this.priority;
    }
}
