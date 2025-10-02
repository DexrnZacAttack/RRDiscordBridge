package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.Arrays;
import java.util.Objects;

public class PoseidonServer extends BukkitServer {
    public PoseidonServer(Server server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(Bukkit.getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }
}
