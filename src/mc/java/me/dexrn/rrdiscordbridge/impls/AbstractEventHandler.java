package me.dexrn.rrdiscordbridge.impls;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import java.util.function.Function;

// TODO: this can just be used for general purpose player/server type sync, and we should name it
// like so.
/**
 * Base class meant to make it so that I don't have to overload every single method everytime I need
 * to return a different server/player
 *
 * @param <ServerT> Server class
 * @param <PlayerT> Player class
 * @param <MinecraftServerT> Server constructor input class (e.g. MinecraftServer)
 * @param <MinecraftPlayerT> Player constructor input class (e.g. Player)
 */
public abstract class AbstractEventHandler<
        ServerT extends IServer,
        PlayerT extends IPlayer,
        MinecraftServerT,
        MinecraftPlayerT> { // what if I used cpp convention instead
    private final Function<MinecraftServerT, ServerT> serverFactory;
    private final Function<MinecraftPlayerT, PlayerT> playerFactory; // the Entity Factory

    public AbstractEventHandler(
            Function<MinecraftServerT, ServerT> server,
            Function<MinecraftPlayerT, PlayerT> player) {
        RRDiscordBridge.logger.info("Registered mod event handler '%s'", getClass().getName());

        this.serverFactory = server;
        this.playerFactory = player;
    }

    public PlayerT createPlayer(MinecraftPlayerT p) {
        return playerFactory.apply(p);
    }

    public ServerT createServer(MinecraftServerT s) {
        return serverFactory.apply(s);
    }
}
