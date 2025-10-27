package me.dexrn.rrdiscordbridge.neoforge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class NeoForgeCopperPlayer extends NeoForgePlayer {
    public NeoForgeCopperPlayer(ServerPlayer player) {
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
        return new NeoForgeCopperServer(player.level().getServer());
    }
}
