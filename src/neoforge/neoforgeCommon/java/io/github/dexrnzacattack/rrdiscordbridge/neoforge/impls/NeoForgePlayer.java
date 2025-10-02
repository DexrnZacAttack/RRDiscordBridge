package io.github.dexrnzacattack.rrdiscordbridge.neoforge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class NeoForgePlayer extends ModernMinecraftPlayer {
    public NeoForgePlayer(ServerPlayer player) {
        super(player);
    }
}
