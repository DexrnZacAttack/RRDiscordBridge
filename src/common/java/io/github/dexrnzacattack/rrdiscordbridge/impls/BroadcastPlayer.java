package io.github.dexrnzacattack.rrdiscordbridge.impls;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

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
