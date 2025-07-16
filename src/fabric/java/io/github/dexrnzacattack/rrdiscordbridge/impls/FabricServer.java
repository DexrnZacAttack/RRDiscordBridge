package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

public class FabricServer implements IServer {
    MinecraftServer server;

    public FabricServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public Set<IPlayer> getOperators() {
        return Set.of();
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public InputStream getResource(String path) {
        return null;
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getMotd() {
        return server.getMotd();
    }

    @Override
    public String getVersion() {
        return server.getServerVersion();
    }

    @Override
    public String getSoftwareName() {
        return server.getServerModName();
    }
}
