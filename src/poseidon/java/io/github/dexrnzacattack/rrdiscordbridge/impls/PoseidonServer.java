package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Objects;

public class PoseidonServer extends BukkitServer {
    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(Bukkit.getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }
}
