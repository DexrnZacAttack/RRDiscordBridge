package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.server.level.ServerPlayer;

public class NeoForgePlayer extends ModernMinecraftPlayer {
    public NeoForgePlayer(ServerPlayer player) {
        super(player);
    }
}
