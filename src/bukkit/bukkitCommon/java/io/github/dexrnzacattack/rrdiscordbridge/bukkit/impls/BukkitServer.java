package io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class BukkitServer implements IServer {
    protected final Server server;

    public BukkitServer(Server server) {
        this.server = server;
    }

    @Override
    public void broadcastMessage(String message) {
        server.broadcastMessage(message);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(server.getOnlinePlayers())
                .map(OfflinePlayer::getPlayer)
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
                .map(BukkitPlayer::getName) // might want to just make it call getName on
                // OfflinePlayer instead
                .toArray(String[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new BukkitPlayer(server.getPlayer(name));
    }

    @Override
    public InputStream getResource(String path) {
        return server.getClass().getResourceAsStream(path);
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
    public String getMotd() {
        return server.getMotd();
    }

    @Override
    public String getVersion() {
        return server.getVersion();
    }

    @Override
    public String getSoftwareName() {
        return server.getName();
    }

    @Override
    public void runCommand(String command) {
        server.dispatchCommand(server.getConsoleSender(), command);
        Events.onServerCommand(command);
    }
}
