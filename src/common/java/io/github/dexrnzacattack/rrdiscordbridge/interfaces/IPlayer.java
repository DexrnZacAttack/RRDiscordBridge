package io.github.dexrnzacattack.rrdiscordbridge.interfaces;

/**
 * Common player methods
 *
 * <p>To be implemented for each server software supported by this plugin
 */
public interface IPlayer {
    /**
     * @return {@code true} if the player is an operator
     */
    boolean isOperator();

    /**
     * @return The player's username
     */
    String getName();

    /** Sends a personal message to the player */
    void sendMessage(String message);

    /**
     * @return {@code true} if the player has joined the server before
     */
    boolean hasPlayedBefore();
}
