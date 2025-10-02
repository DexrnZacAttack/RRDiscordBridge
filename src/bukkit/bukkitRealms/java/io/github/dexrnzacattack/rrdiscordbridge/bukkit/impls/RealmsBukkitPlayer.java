package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import org.bukkit.entity.Player;

public class RealmsBukkitPlayer extends BukkitPlayer {
    public RealmsBukkitPlayer(Player player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new RealmsBukkitServer(player.getServer());
    }
}
