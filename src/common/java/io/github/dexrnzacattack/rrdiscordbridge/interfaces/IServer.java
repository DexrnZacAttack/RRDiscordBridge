package io.github.dexrnzacattack.rrdiscordbridge.interfaces;

import java.io.InputStream;

/**
 * Common server methods
 *
 * <p>To be implemented for each server software supported by this plugin
 */
public interface IServer {
    /** Broadcasts a message to the entire server */
    void broadcastMessage(String message);

    /**
     * @return A list of all online players
     */
    IPlayer[] getOnlinePlayers();

    /**
     * @return A list of all operators on the server
     */
    String[] getOperators();

    /**
     * @return A player with the given username (if they've joined before)
     */
    IPlayer getPlayer(String name);

    /**
     * @return A resource from the game's jar file
     */
    InputStream getResource(String path);

    /**
     * @return A value of the maximum amount of players that can be online at once
     */
    int getMaxPlayers();

    /**
     * @return the server's name
     */
    String getName();

    /**
     * @return The server's MOTD
     */
    String getMotd();

    /**
     * @return The server's version
     */
    String getVersion();

    /**
     * @return The server software's name
     */
    String getSoftwareName();
}
