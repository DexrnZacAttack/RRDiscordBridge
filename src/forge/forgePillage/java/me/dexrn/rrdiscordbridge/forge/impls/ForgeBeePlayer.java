package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

public class ForgePillagePlayer extends ForgeNetherPlayer {
    public ForgePillagePlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public IServer getServer() {
        return new ForgePillageServer(player.getServer());
    }
}
