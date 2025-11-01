package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftPlayer;

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
