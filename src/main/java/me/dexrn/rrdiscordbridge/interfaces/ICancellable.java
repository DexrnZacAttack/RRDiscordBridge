package me.dexrn.rrdiscordbridge.interfaces;

/**
 * Based off of Bukkit's Cancellable
 *
 * <p>To be implemented for each server software supported by this plugin
 */
public interface ICancellable {
    /** Cancels an event */
    void cancel();

    /** Uncancels an event */
    void uncancel();
}
