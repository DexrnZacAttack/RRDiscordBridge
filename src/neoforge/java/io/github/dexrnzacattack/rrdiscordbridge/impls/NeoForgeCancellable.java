package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;

import net.neoforged.bus.api.ICancellableEvent;

public class NeoForgeCancellable implements ICancellable {
    protected ICancellableEvent event;

    public NeoForgeCancellable(ICancellableEvent event) {
        this.event = event;
    }

    @Override
    public void cancel() {
        event.setCanceled(true);
    }

    @Override
    public void uncancel() {
        event.setCanceled(false);
    }
}
