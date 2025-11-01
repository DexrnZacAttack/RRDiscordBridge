package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Objects;
import java.util.UUID;

public class ForgeCliffsServer extends ForgeServer {
    // todo yes I have to keep copying these other methods everywhere otherwise some weird ass
    // remapping issues happen and I don't understand it
    public ForgeCliffsServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeCliffsPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeCliffsPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgeCliffsPlayer(server.getPlayerList().getPlayer(id));
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList()
                .broadcastMessage(new TextComponent(message), ChatType.SYSTEM, Util.NIL_UUID);
    }

    @Override
    public String[] getOperators() {
        return server.getPlayerList().getOps().getUserList();
    }

    @Override
    public void runCommand(String command) {
        if (server.isDedicatedServer())
            ((DedicatedServer) server)
                    .handleConsoleInput(command, server.createCommandSourceStack());
    }

    @Override
    public String getVersion() {
        return "Minecraft " + server.getServerVersion();
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    @Override
    public String getMotd() {
        return server.getMotd();
    }
}
