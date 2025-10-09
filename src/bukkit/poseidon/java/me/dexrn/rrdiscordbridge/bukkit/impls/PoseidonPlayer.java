package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import org.bukkit.entity.Player;

public class PoseidonPlayer extends BukkitPlayer {
    public PoseidonPlayer(Player player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new PoseidonServer(player.getServer());
    }
}
