package io.github.dexrnzacattack.rrdiscordbridge.forge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class ForgePlayer extends ModernMinecraftPlayer {
    public ForgePlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new ForgeServer(player.getServer());
    }
}
