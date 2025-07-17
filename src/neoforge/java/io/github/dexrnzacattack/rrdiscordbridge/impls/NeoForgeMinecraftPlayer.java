package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class NeoForgeMinecraftPlayer implements IPlayer {
    protected final Player player;

    public NeoForgeMinecraftPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isOperator() {
        return player.getPermissionLevel() > 1;
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
    public boolean hasPlayedBefore() {
        return true;
    }
}
