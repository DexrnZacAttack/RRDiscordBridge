package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OldBukkitServer extends Server {
    public void broadcastMessage(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(Bukkit.getServer().getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(Player::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public Set<IPlayer> getOperators() {
        return Bukkit.getServer().getOperators().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(Player::new)
                .collect(Collectors.toSet());
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new Player(Bukkit.getServer().getPlayer(name));
    }

    @Override
    public InputStream getResource(String path) {
        return Bukkit.getServer().getClass().getResourceAsStream(path);
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getServer().getMaxPlayers();
    }

    @Override
    public String getName() {
        return Bukkit.getServer().getServerName();
    }

    @Override
    public String getVersion() {
        return Bukkit.getServer().getVersion();
    }

    @Override
    public String getSoftwareName() {
        return Bukkit.getServer().getName();
    }
}
