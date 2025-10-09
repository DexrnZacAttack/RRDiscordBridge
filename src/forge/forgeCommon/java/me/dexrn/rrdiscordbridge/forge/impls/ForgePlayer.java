package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

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
