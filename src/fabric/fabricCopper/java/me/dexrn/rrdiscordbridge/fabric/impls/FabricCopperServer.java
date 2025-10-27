package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class FabricCopperServer extends FabricServer {

    public FabricCopperServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricCopperPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricCopperPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new FabricCopperPlayer(server.getPlayerList().getPlayer(id));
    }
}
