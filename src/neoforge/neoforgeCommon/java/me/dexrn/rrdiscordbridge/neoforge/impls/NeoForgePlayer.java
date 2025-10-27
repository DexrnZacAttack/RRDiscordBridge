package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class NeoForgePlayer extends ModernMinecraftPlayer {
    public NeoForgePlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new NeoForgeServer(player.getServer());
    }
}
