package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class FabricVexPlayer extends ModernMinecraftPlayer {

    public FabricVexPlayer(ServerPlayer player) {
        super(player);
    }
}
