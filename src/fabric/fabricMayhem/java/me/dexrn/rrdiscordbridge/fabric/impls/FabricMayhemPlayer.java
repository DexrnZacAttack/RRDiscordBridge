package me.dexrn.rrdiscordbridge.fabric.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.Objects;

public class FabricMayhemPlayer extends FabricPlayer {

    public FabricMayhemPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.level().getServer())
                        .getProfilePermissions(player.nameAndId()).level().isEqualOrHigherThan(PermissionLevel.ADMINS);
    }

    @Override
    public IServer getServer() {
        return new FabricMayhemServer(player.level().getServer());
    }
}
