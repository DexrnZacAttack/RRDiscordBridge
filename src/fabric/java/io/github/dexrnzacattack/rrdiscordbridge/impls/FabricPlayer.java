package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.minecraft.world.entity.player.Player;

// unfinished
public class FabricPlayer implements IPlayer {
    Player player;

    public FabricPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean isOperator() {
        return player.getPermissionLevel() > 0;
    }

    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public void sendMessage(String message) {}

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }
}
