package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.ModList;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class ForgeNetherServer implements IServer {
    private final MinecraftServer server;

    public ForgeNetherServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeNetherPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeNetherPlayer(server.getPlayerList().getPlayerByName(name));
    }

    /**
     * @return A resource from the game's jar file
     */
    @Override
    public InputStream getResource(String path) {
        return null;
    }

    public IPlayer getPlayer(UUID id) {
        return new ForgeNetherPlayer(server.getPlayerList().getPlayer(id));
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

    /**
     * @return the server's name
     */
    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getMotd() {
        return server.getMotd();
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
}
