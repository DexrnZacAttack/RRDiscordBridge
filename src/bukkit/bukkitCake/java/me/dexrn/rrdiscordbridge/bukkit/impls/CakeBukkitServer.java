package me.dexrn.rrdiscordbridge.bukkit.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import org.bukkit.Player;
import org.bukkit.Server;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class CakeBukkitServer implements IServer {
    protected final Server server;

    public CakeBukkitServer(Server server) {
        this.server = server;
    }

    public void broadcastMessage(String message) {
        for (Player p : server.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return Arrays.stream(server.getOnlinePlayers())
                .filter(Objects::nonNull)
                .map(CakeBukkitPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String[] getOperators() {
        return new String[0];
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new CakeBukkitPlayer(server.getPlayer(name));
    }

    @Override
    public int getMaxPlayers() {
        return -1;
    }

    @Override
    public String getName() {
        return "";
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
    public InputStream getResource(String path) {
        return server.getClass().getResourceAsStream(path);
    }

    @Override
    public String getMotd() {
        return "";
    }

    @Override
    public void runCommand(String command) {}
}
