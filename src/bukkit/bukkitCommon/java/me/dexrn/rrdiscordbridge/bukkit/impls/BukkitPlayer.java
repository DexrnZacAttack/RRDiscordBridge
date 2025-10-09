package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

public class BukkitPlayer implements IPlayer {
    protected final org.bukkit.entity.Player player;

    public BukkitPlayer(org.bukkit.entity.Player player) {
        this.player = player;
    }

    @Override
    public boolean isOperator() {
        return player.isOp();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPlayedBefore() {
        return player.hasPlayedBefore();
    }

    @Override
    public IServer getServer() {
        return new BukkitServer(player.getServer());
    }
}
