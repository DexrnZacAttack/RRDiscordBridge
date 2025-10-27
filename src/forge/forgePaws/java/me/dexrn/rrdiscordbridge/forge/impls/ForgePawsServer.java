package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.ModList;

import java.util.Objects;
import java.util.UUID;

public class ForgePawsServer extends ForgeServer {
    public ForgePawsServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public String getSoftwareName() {
        String name =
                ModList.get()
                        .getModContainerById("forge")
                        .map(container -> container.getModInfo().getDisplayName())
                        .orElse("Forge-based");

        String version =
                ModList.get()
                        .getModContainerById("forge")
                        .map(container -> container.getModInfo().getVersion().toString())
                        .orElse("");

        return name + " " + version;
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgePawsPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgePawsPlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgePawsPlayer(server.getPlayerList().getPlayer(id));
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
    public String getVersion() {
        return "Minecraft " + server.getServerVersion();
    }

    @Override
    public void runCommand(String command) {
        if (server.isDedicatedServer())
            ((DedicatedServer) server)
                    .handleConsoleInput(command, server.createCommandSourceStack());
    }
}
