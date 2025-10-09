package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;

/** Cancellable class */
public class Cancellable implements ICancellable {
    private boolean cancelled = false;

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public void uncancel() {
        cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
