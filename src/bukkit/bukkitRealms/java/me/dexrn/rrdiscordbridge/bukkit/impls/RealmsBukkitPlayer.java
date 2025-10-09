package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

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
