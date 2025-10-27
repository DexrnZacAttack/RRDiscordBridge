package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class NeoForgeCopperServer extends NeoForgeServer {
    public NeoForgeCopperServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(NeoForgeCopperPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new NeoForgeCopperPlayer(server.getPlayerList().getPlayerByName(name));
    }

    public IPlayer getPlayer(UUID id) {
        return new NeoForgeCopperPlayer(server.getPlayerList().getPlayer(id));
    }
}
