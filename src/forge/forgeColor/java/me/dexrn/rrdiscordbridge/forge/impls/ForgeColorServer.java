package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeModContainer;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class ForgeColorServer implements IServer {
    private final MinecraftServer server;

    public ForgeColorServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeColorPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeColorPlayer(server.getPlayerList().getPlayerByUsername(name));
    }

    /**
     * @return A resource from the game's jar file
     */
    @Override
    public InputStream getResource(String path) {
        return null;
    }

    public IPlayer getPlayer(UUID id) {
        return new ForgeColorPlayer(server.getPlayerList().getPlayerByUUID(id));
    }

    @Override
    public void broadcastMessage(String message) {
        server.getPlayerList().sendMessage(new TextComponentString(message), true);
    }

    @Override
    public String[] getOperators() {
        return server.getPlayerList().getOppedPlayerNames();
    }

    @Override
    public void runCommand(String command) {
        if (server.isDedicatedServer())
            ((DedicatedServer) server).addPendingCommand(command, server);
    }

    @Override
    public String getVersion() {
        return "Minecraft " + server.getMinecraftVersion();
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
        return server.getMOTD();
    }

    @Override
    public String getSoftwareName() {
        final ForgeModContainer c = ForgeModContainer.getInstance();

        return c.getName() + " " + c.getVersion();
    }
}
