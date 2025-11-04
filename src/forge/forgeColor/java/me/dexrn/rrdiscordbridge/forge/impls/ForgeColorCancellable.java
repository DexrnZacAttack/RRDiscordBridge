package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.ICancellable;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ForgeColorCancellable implements ICancellable {
    protected Event event;

    public ForgeColorCancellable(Event event) {
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
