package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Objects;
import java.util.UUID;

public class ForgeWildServer extends ForgeServer {
    public ForgeWildServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public String getVersion() {
        return "Minecraft " + server.getServerVersion();
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeWildPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeWildPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgeWildPlayer(server.getPlayerList().getPlayer(id));
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().broadcastSystemMessage(Component.literal(message), ChatType.SYSTEM);
    }

    @Override
    public String[] getOperators() {
        return server.getPlayerList().getOps().getUserList();
    }

    @Override
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    @Override
    public String getMotd() {
        return server.getMotd();
    }

    @Override
    public void runCommand(String command) {
        if (server.isDedicatedServer())
            ((DedicatedServer) server)
                    .handleConsoleInput(command, server.createCommandSourceStack());
    }
}
