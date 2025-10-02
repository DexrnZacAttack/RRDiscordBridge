package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

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
