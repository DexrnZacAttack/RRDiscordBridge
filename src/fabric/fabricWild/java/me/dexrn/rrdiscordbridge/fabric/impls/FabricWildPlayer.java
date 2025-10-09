package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class FabricWildPlayer extends FabricPlayer {
    public FabricWildPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new FabricWildServer(player.getServer());
    }
}
