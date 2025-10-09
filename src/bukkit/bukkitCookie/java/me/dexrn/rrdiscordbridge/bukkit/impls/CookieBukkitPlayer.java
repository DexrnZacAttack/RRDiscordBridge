package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import org.bukkit.entity.Player;

public class CookieBukkitPlayer extends BukkitPlayer {
    public CookieBukkitPlayer(Player player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new CookieBukkitServer(player.getServer());
    }
}
