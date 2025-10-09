package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftServer;

import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.ModList;

public class NeoForgeServer extends ModernMinecraftServer {
    public NeoForgeServer(MinecraftServer server) {
        super(server);
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
}
