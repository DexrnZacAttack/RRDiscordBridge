package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Objects;

public class ModernBukkitServer extends BukkitServer {
    @Override
    public IPlayer[] getOnlinePlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }
}
