package org.bukkit.craftbukkit;

import org.bukkit.Player;

public class CraftPlayer implements Player {
    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public String getName() {
        return "STUB";
    }

    @Override
    public void sendMessage(String message) {}

    public boolean isOp() {
        return false;
    }
}
