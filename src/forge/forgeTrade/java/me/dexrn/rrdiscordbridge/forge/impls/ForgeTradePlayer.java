package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ForgeTradePlayer extends ForgePlayer {
    public ForgeTradePlayer(ServerPlayer player) {
        super(player);
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
        return new ForgeTradeServer(player.getServer());
    }
}
