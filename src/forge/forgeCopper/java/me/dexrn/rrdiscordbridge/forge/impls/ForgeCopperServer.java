package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class ForgeCopperServer extends ForgePawsServer {
    public ForgeCopperServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeCopperPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeCopperPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgeCopperPlayer(server.getPlayerList().getPlayer(id));
    }
}
