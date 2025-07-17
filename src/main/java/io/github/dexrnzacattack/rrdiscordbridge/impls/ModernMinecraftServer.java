package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

// TODO: had to move this back in here cuz couldn't get other sourceset to work
// This is meant to be just a core MinecraftServer and MinecraftPlayer that we extend from on Fabric
// and NeoForge.
public class ModernMinecraftServer implements IServer {
    protected final MinecraftServer server;

    public ModernMinecraftServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ModernMinecraftPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String[] getOperators() {
        return server.getPlayerList().getOps().getUserList();
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ModernMinecraftPlayer(server.getPlayerList().getPlayerByName(name));
    }

    public IPlayer getPlayer(UUID id) {
        return new ModernMinecraftPlayer(server.getPlayerList().getPlayer(id));
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
        return "Minecraft " + server.getServerVersion();
    }

    @Override
    public String getSoftwareName() {
        return "Minecraft";
    }
}
