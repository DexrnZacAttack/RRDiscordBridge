package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class FabricWildServer extends FabricServer {
    public FabricWildServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().broadcastSystemMessage(Component.literal(message), ChatType.SYSTEM);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricWildPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricWildPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new FabricWildPlayer(server.getPlayerList().getPlayer(id));
    }
}
