package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftServer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;
import java.util.Optional;

public class FabricVexServer extends ModernMinecraftServer {

    public FabricVexServer(MinecraftServer server) {
        super(server);
    }

    @Override
    public IPlayer[] getOnlinePlayers() {
        return PlayerLookup.all(server).stream()
                .filter(Objects::nonNull)
                .map(FabricVexPlayer::new)
                .toArray(IPlayer[]::new);
    }

    @Override
    public String getSoftwareName() {
        Optional<ModContainer> container =
                FabricLoader.getInstance().getModContainer("fabricloader");

        return container.map(m -> m.getMetadata().getName()).orElse("Fabric-based")
                + " "
                + container.map(m -> m.getMetadata().getVersion().getFriendlyString()).orElse("");
    }
}
