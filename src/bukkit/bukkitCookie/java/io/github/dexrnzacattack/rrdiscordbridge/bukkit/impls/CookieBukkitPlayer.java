package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

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
