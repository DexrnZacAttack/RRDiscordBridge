package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class FabricPlayer extends ModernMinecraftPlayer {

    public FabricPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new FabricServer(player.getServer());
    }
}
