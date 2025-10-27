package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class FabricPotPlayer extends FabricPlayer {
    public FabricPotPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                        .getProfilePermissions(player.getGameProfile())
                > 1;
    }

    @Override
    public IServer getServer() {
        return new FabricPotServer(player.getServer());
    }
}
