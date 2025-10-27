package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class FabricPotServer extends FabricServer {
    public FabricPotServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricPotPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricPotPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new FabricPotPlayer(server.getPlayerList().getPlayer(id));
    }
}
