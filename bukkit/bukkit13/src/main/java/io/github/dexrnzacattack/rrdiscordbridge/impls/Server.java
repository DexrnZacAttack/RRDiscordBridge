package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Server implements IServer {
    @Override
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(Bukkit.getOnlinePlayers())
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(Player::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public Set<IPlayer> getOperators() {
        return Bukkit.getOperators().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(Player::new)
                .collect(Collectors.toSet());
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new Player(Bukkit.getPlayer(name));
    }

    @Override
    public InputStream getResource(String path) {
        return Bukkit.getServer().getClass().getResourceAsStream(path);
    }

    @Override
    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    @Override
    public String getName() {
        return Bukkit.getServerName();
    }

    @Override
    public String getMotd() {
        return Bukkit.getMotd();
    }

    @Override
    public String getVersion() {
        return Bukkit.getVersion();
    }

    @Override
    public String getSoftwareName() {
        return Bukkit.getName();
    }
}
