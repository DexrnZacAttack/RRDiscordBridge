package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.UUID;

public class FabricNetherServer extends FabricServer {
    public FabricNetherServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new FabricNetherPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new FabricNetherPlayer(server.getPlayerList().getPlayer(id));
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList()
                .broadcastMessage(new TextComponent(message), ChatType.SYSTEM, Util.NIL_UUID);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricNetherPlayer::new)
                .toArray(IPlayer[]::new);
    }
}
