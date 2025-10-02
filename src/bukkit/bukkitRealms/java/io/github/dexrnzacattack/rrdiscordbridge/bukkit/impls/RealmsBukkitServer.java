package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.util.Objects;

public class RealmsBukkitServer extends BukkitServer {
    public RealmsBukkitServer(Server server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getOnlinePlayers().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }
}
