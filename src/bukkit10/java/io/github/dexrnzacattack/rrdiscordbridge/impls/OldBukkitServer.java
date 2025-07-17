package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class OldBukkitServer extends BukkitServer {
    public void broadcastMessage(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(Bukkit.getServer().getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String[] getOperators() {
        return Bukkit.getServer().getOperators().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .map(BukkitPlayer::getName)
                .toArray(String[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new BukkitPlayer(Bukkit.getServer().getPlayer(name));
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
