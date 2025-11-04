package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.ModList;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class ForgeAquaticServer implements IServer {
    private final MinecraftServer server;

    public ForgeAquaticServer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(ForgeAquaticPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgeAquaticPlayer(server.getPlayerList().getPlayerByUsername(name));
    }

    /**
     * @return A resource from the game's jar file
     */
    @Override
    public InputStream getResource(String path) {
        return null;
    }

    public IPlayer getPlayer(UUID id) {
        return new ForgeAquaticPlayer(server.getPlayerList().getPlayerByUUID(id));
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
            ((DedicatedServer) server).handleConsoleInput(command, server.getCommandSource());
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
