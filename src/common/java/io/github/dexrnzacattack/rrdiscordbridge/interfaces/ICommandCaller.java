package io.github.dexrnzacattack.rrdiscordbridge.interfaces;

/**
 * Based off of Bukkit's CommandSender
 *
 * <p>To be implemented for each server software supported by this plugin
 */
public interface ICommandCaller {
    /** Responds to the invoker with a message */
    void respond(String message);

    /**
     * @return The name of the player that invoked the command
     */
    String getName();
}
