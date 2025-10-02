package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class FabricTradePlayer extends ModernMinecraftPlayer {

    public FabricTradePlayer(ServerPlayer player) {
        super(player);
    }
}
