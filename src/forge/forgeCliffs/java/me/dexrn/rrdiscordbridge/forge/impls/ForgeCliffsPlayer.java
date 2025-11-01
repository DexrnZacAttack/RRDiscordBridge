package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class ForgeCliffsPlayer extends ForgePlayer {
    public ForgeCliffsPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public void sendMessage(String message) {
        player.displayClientMessage(new TextComponent(message), false);
    }

    @Override
    public IServer getServer() {
        return new ForgeCliffsServer(player.getServer());
    }
}
