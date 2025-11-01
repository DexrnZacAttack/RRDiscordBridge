package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftServer;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ModList;

import java.util.Objects;
import java.util.UUID;

public class ForgeServer extends ModernMinecraftServer {
    public ForgeServer(MinecraftServer server) {
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
                .map(ForgePlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public IPlayer getPlayer(String name) {
        return new ForgePlayer(server.getPlayerList().getPlayerByName(name));
    }

    @Override
    public IPlayer getPlayer(UUID id) {
        return new ForgePlayer(server.getPlayerList().getPlayer(id));
    }
}
