package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class FabricCopperPlayer extends FabricPlayer {

    public FabricCopperPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.level().getServer())
                        .getProfilePermissions(player.nameAndId())
                > 1;
    }

    @Override
    public IServer getServer() {
        return new FabricCopperServer(player.level().getServer());
    }
}
