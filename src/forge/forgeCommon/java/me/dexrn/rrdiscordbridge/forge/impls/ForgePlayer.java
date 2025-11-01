package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.ModernMinecraftPlayer;

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
