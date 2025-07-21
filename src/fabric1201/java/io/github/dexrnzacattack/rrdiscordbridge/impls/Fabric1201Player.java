package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftPlayer;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class Fabric1201Player extends ModernMinecraftPlayer {
    public Fabric1201Player(ServerPlayer player) {
        super(player);
    }

    @Override
    public void sendMessage(String message) {
        player.displayClientMessage(new TextComponent(message), false);
    }
}
