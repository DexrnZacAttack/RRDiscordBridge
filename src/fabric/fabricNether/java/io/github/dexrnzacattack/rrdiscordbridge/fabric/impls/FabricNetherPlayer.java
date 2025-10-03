package io.github.dexrnzacattack.rrdiscordbridge.fabric.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetherPlayer extends FabricPlayer {
    public FabricNetherPlayer(ServerPlayer player) {
        super(player);
    }

    @Override
    public void sendMessage(String message) {
        player.displayClientMessage(new TextComponent(message), false);
    }

    @Override
    public IServer getServer() {
        return new FabricNetherServer(player.getServer());
    }
}
