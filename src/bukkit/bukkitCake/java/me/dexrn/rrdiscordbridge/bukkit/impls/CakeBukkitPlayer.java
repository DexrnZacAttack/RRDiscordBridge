package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.bukkit.CakeBukkitPlugin;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import org.bukkit.craftbukkit.CraftPlayer;

public class CakeBukkitPlayer implements IPlayer {
    protected final org.bukkit.Player player;

    public CakeBukkitPlayer(org.bukkit.Player player) {
        this.player = player;
    }

    @Override
    public boolean isOperator() {
        if (player instanceof CraftPlayer) return ((CraftPlayer) player).isOp();

        return false;
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
        return true;
    }

    @Override
    public IServer getServer() {
        return new CakeBukkitServer(CakeBukkitPlugin.server);
    }
}
