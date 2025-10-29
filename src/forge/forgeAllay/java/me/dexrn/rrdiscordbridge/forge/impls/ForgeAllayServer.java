package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Objects;
import java.util.UUID;

public class ForgeAllayServer extends ForgeServer {
    public ForgeAllayServer(MinecraftServer server) {
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
                .map(ForgeAllayPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeAllayPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgeAllayPlayer(server.getPlayerList().getPlayer(id));
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
    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    @Override
    public String getMotd() {
        return server.getMotd();
    }

    @Override
    public String getSoftwareName() {
        return "Minecraft";
    }

    @Override
    public void runCommand(String command) {
        if (server.isDedicatedServer())
            ((DedicatedServer) server)
                    .handleConsoleInput(command, server.createCommandSourceStack());
    }
}
