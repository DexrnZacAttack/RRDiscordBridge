package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftPlayer;

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
