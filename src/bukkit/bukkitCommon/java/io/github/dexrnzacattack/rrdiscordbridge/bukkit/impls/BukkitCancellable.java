package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;

public class BukkitCancellable implements ICancellable {
    private final org.bukkit.event.Cancellable inst;

    public BukkitCancellable(org.bukkit.event.Cancellable inst) {
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
