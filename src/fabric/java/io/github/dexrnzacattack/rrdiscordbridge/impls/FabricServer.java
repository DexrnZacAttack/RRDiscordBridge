package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class FabricServer implements IServer {
    protected final MinecraftServer server;

    public FabricServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
    }

    @Override
    public String[] getOperators() {
        return server.getPlayerList().getOps().getUserList();
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricPlayer(server.getPlayerList().getPlayerByName(name));
    }

    public IPlayer getPlayer(UUID id) {
        return new FabricPlayer(server.getPlayerList().getPlayer(id));
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
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String getSoftwareName() {
        Optional<ModContainer> container =
                FabricLoader.getInstance().getModContainer("fabricloader");

        return container.map(m -> m.getMetadata().getName()).orElse("Fabric-based")
                + " "
                + container.map(m -> m.getMetadata().getVersion().getFriendlyString()).orElse("");
    }
}
