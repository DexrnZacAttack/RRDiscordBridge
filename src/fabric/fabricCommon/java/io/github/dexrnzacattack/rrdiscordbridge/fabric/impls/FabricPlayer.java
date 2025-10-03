package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class FabricPlayer extends ModernMinecraftPlayer {

    public FabricPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new FabricServer(player.getServer());
    }
}
