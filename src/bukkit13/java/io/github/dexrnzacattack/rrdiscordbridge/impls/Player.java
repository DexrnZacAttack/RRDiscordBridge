package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

public class Player implements IPlayer {
    private final org.bukkit.entity.Player inst;

    public Player(org.bukkit.entity.Player player) {
        inst = player;
    }

    @Override
    public boolean isOperator() {
        return inst.isOp();
    }

    @Override
    public String getName() {
        return inst.getName();
    }

    @Override
    public void sendMessage(String message) {
        inst.sendMessage(message);
    }

    @Override
    public boolean hasPlayedBefore() {
        return inst.hasPlayedBefore();
    }
}
