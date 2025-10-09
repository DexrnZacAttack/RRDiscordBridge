package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICancellable;

import net.minecraftforge.eventbus.api.Event;

public class ForgeCancellable implements ICancellable {
    protected Event event;

    public ForgeCancellable(Event event) {
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
