package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class FabricPlayer extends ModernMinecraftPlayer {

    public FabricPlayer(ServerPlayer player) {
        super(player);
    }
}
