package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftServer;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;

import java.util.Objects;
import java.util.UUID;

public class NeoForgeServer extends ModernMinecraftServer {
    public NeoForgeServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return server.getPlayerList().getPlayers().stream()
                .filter(Objects::nonNull)
                .map(NeoForgePlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String getSoftwareName() {
        String name =
                ModList.get()
                        .getModContainerById("neoforge")
                        .map(container -> container.getModInfo().getDisplayName())
                        .orElse("NeoForge-based");

        String version =
                ModList.get()
                        .getModContainerById("neoforge")
                        .map(container -> container.getModInfo().getVersion().toString())
                        .orElse("");

        return name + " " + version;
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new NeoForgePlayer(server.getPlayerList().getPlayerByName(name));
    }

    public IPlayer getPlayer(UUID id) {
        return new NeoForgePlayer(server.getPlayerList().getPlayer(id));
    }
}
