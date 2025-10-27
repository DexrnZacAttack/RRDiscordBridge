package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ForgePawsPlayer extends ForgePlayer {
    public ForgePawsPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                        .getProfilePermissions(player.getGameProfile())
                > 1;
    }

    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public void sendMessage(String message) {
        player.displayClientMessage(Component.literal(message), false);
    }

    @Override
    public IServer getServer() {
        return new ForgePawsServer(player.getServer());
    }
}
