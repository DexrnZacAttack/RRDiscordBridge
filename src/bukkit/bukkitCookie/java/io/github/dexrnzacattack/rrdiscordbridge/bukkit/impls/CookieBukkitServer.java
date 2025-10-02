package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.util.Arrays;
import java.util.Objects;

public class CookieBukkitServer extends BukkitServer {
    public CookieBukkitServer(Server server) {
        super(server);
    }

    public void broadcastMessage(String message) {
        server.broadcastMessage(message);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(server.getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String[] getOperators() {
        return server.getOperators().stream()
                .map(OfflinePlayer::getPlayer)
                .filter(Objects::nonNull)
                .map(BukkitPlayer::new)
                .map(BukkitPlayer::getName)
                .toArray(String[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new BukkitPlayer(server.getPlayer(name));
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    @Override
    public String getName() {
        return server.getServerName();
    }

    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @Override
    public String getSoftwareName() {
        return server.getName();
    }
}
