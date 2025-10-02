package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class FabricWildPlayer extends ModernMinecraftPlayer {
    public FabricWildPlayer(ServerPlayer player) {
        super(player);
    }
}
