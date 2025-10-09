package me.dexrn.rrdiscordbridge.impls;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.instance;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

/** A default player created for broadcasting messages */
public class BroadcastPlayer implements IPlayer {
    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public String getName() {
        return "Server (Broadcast)";
    }

    @Override
    public void sendMessage(String message) {}

    @Override
    public boolean hasPlayedBefore() {
        return true;
    }

    @Override
    public IServer getServer() {
        return instance.getServer();
    }
}
