package io.github.dexrnzacattack.rrdiscordbridge.forge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ForgeTrailsPlayer extends ForgePlayer {
    public ForgeTrailsPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public boolean isOperator() {
        return player.getServer().getProfilePermissions(player.getGameProfile()) > 1;
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
        return new ForgeTrailsServer(player.getServer());
    }
}
