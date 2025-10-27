package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ForgeCopperPlayer extends ForgePawsPlayer {
    public ForgeCopperPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.level().getServer()) // what the fuck were they smoking
                        .getProfilePermissions(player.nameAndId())
                > 1;
    }

    @Override
    public IServer getServer() {
        return new ForgeCopperServer(player.level().getServer());
    }
}
