package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;

public class Cancellable implements ICancellable {
    private final org.bukkit.event.Cancellable inst;

    public Cancellable(org.bukkit.event.Cancellable inst) {
        this.inst = inst;
    }

    @Override
    public void cancel() {
        inst.setCancelled(true);
    }

    @Override
    public void uncancel() {
        inst.setCancelled(false);
    }
}
